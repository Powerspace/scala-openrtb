package com.powerspace.openrtb.json.util

import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import io.circe._
import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.decoding.ConfiguredDecoder
import scalapb.{ExtendableMessage, GeneratedEnumCompanion, UnknownFieldSet}
import shapeless.Lazy

import scala.reflect.ClassTag
import scala.util.Try

object EncodingUtils {

  import io.circe.generic.extras.semiauto._
  import scala.reflect.runtime.universe.TypeTag
  import scala.reflect.runtime.{currentMirror => cm}
  import PrimitivesUtils._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults

  def extendedEncoder[Ext <: ExtendableMessage[Ext]](implicit encoder: Lazy[ConfiguredObjectEncoder[Ext]], er: ExtensionRegistry, tag: ClassTag[Ext]): Encoder[Ext] =
    er.encoderWithExtensions[Ext](baseEncoder = openRtbEncoder)
  def extendedDecoder[Ext <: ExtendableMessage[Ext]](implicit encoder: Lazy[ConfiguredDecoder[Ext]], er: ExtensionRegistry, tag: ClassTag[Ext]): Decoder[Ext] =
    er.decoderWithExtensions[Ext](baseDecoder = openRtbDecoder)

  def openRtbEncoder[A](implicit encoder: Lazy[ConfiguredObjectEncoder[A]]): Encoder[A] = deriveEncoder[A](encoder).cleanRtb
  def openRtbDecoder[A](implicit decoder: Lazy[ConfiguredDecoder[A]]): Decoder[A] = deriveDecoder[A](decoder)

  /**
    * Unknown fields Encoder
    * @todo move away from utils
    */
  implicit val unknownFieldsEncoder: Encoder[UnknownFieldSet] = (_: UnknownFieldSet) => Json.Null
  implicit val unknownFieldsDecoder: Decoder[UnknownFieldSet] = (_: HCursor) => Right(UnknownFieldSet.empty)

  implicit class EncoderEnhancement[T](encoder: Encoder[T]) {

    /**
      * Clean up the JSON file from empty fields/arrays unless present within the given list
      */
    def clean(toKeep: Seq[String] = Seq()): Encoder[T] = {
      encoder.mapJson(jsonObject =>
        jsonObject.mapObject(obj => obj.filter {
          case (_, Json.Null) => false
          case (id, json) if json.isArray && json.asArray.exists(_.isEmpty) && !toKeep.contains(id) => false
          case _ => true
        })
      )
    }

    /**
      * Transform boolean fields into the related integers
      */
    def transformBooleans: Encoder[T] = {
      encoder.mapJson({
        json =>
          json.mapObject(_.mapValues {
            case boolean if boolean.isBoolean => Json.fromInt(boolean.asBoolean.map(_.toInt).get)
            case other => other
          })
      })
    }

    def cleanRtb: Encoder[T] = renameOneof.clean.transformBooleans

    def rename(renames: Map[String, String]): Encoder[T] = encoder.mapJson({
      _.mapObject(obj => JsonObject.fromIterable(obj.toIterable.map {
        case (key, json) => (renames.getOrElse(key, key), json)
      }))
    })

    def renameOneof: Encoder[T] = encoder.mapJson({
      _.mapObject(obj => JsonObject.fromIterable(obj.toIterable.map {
        case (key, json) => (key.stripSuffix("_oneof"), json)
      }))
    })


    def clean: Encoder[T] = clean(Seq())

  }

  /**
    * Quick access to the decoding result value. Useful for testing
    */
  implicit class DecoderResultEnhancement[T](result: Decoder.Result[T]) {
    def value: T = result.right.get
  }

  /**
    * Allow to generate a JSON integer field from an Enum instance
    */
  def protobufEnumEncoder[T <: _root_.scalapb.GeneratedEnum]: Encoder[T] = {
    enum: T => Json.fromInt(enum.value)
  }

  /**
    * Allow to generate a JSON integer field from an Enum instance
    */
  def protobufEnumDecoder[T <: _root_.scalapb.GeneratedEnum](implicit tag: TypeTag[T]): Decoder[T] = {
    val thisClassCompanion = tag.tpe.typeSymbol.companion.asModule
    val companion = cm
      .reflectModule(thisClassCompanion)
      .instance
      .asInstanceOf[GeneratedEnumCompanion[T]]

    Decoder.decodeInt.emapTry[T](i => {
      Try(companion.fromValue(i))
    })
  }

  /**
    * Allow to generate a JSON field from an oneof PB structure
    */
  def protobufOneofEncoder[Oneof <: _root_.scalapb.GeneratedOneof](partialFunction: PartialFunction[Oneof, Json]): Encoder[Oneof] = {
    oneOf: Oneof => {
        if (oneOf.isEmpty) Json.Null
        else partialFunction.apply(oneOf)
    }
  }

  implicit val booleanDecoder: Decoder[Boolean] = Decoder.decodeBoolean.prepare(cursor => {
    cursor.withFocus(
      _.asNumber.map(
        _.toInt
          .map(_.toBoolean)
          .map(Json.fromBoolean)
          getOrElse Json.False
      ).getOrElse(Json.False))
  })

}