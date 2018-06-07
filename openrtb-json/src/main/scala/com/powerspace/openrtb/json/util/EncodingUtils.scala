package com.powerspace.openrtb.json.util

import io.circe.{Decoder, Encoder, Json, JsonObject}
import scalapb.UnknownFieldSet

object EncodingUtils {

  /**
    * Convert a boolean to the related integer value
    */
  implicit class BooleanEnhancement(b: Boolean) {
    def toInt: Int = if (b) 1 else 0
  }

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
            case json: Json if json.isBoolean => Json.fromInt(json.asBoolean.map(_.toInt).get)
            case json: Json => json
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
    * @todo user would define a list of protobuf extensions to actually encode
    */

  implicit val unknownFieldsEncoder: Encoder[UnknownFieldSet] = (_: UnknownFieldSet) => Json.Null

  /**
    * Allow to generate a JSON field from an oneof PB structure
    */
  def protobufOneofEncoder[Oneof <: _root_.scalapb.GeneratedOneof](partialFunction: PartialFunction[Oneof, Json]): Encoder[Oneof] = {
    oneOf: Oneof => {
        if (oneOf.isEmpty) Json.Null
        else partialFunction.apply(oneOf)
    }
  }
}
