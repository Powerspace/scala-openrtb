package com.powerspace.openrtb.json.util

import io.circe.{Encoder, Json}
import scalapb.UnknownFieldSet

object JsonUtils {

  implicit class BooleanEnhancement(b: Boolean) {
    def toInt: Int = if (b) 1 else 0
  }

  implicit class NoNullsEncoder[T](encoder: Encoder[T]) {

    def clean(toKeep: Seq[String] = Seq()): Encoder[T] = {
      encoder.mapJson(jsonObject =>
        jsonObject.mapObject(obj => obj.filter {
          case (_, Json.Null) => false
          case (id, json) if json.isArray && json.asArray.exists(_.isEmpty) && !toKeep.contains(id) => false
          case _ => true
        })
      )
    }

    def clean: Encoder[T] = clean(Seq())

    def recodeBooleans: Encoder[T] = {
      encoder.mapJson({
        json => json.mapObject(_.mapValues{
          case json: Json if json.isBoolean =>
            Json.fromInt(json.asBoolean.map(_.toInt).get)
          case json: Json =>
            json})
      })
    }
  }

  object NoNullsEncoder {
    final def apply[A](f: A => Seq[(String, Json)], toKeep: Seq[String] = Seq()): Encoder[A] = new Encoder[A] {
      override def apply(a: A): Json = Json.fromFields(f(a))
    }.clean(toKeep)
  }


  def protobufEnumEncoder[T <: _root_.scalapb.GeneratedEnum]: Encoder[T] = {
    (a: T) => Json.fromInt(a.value)
  }

  implicit val unknownFieldsEncoder: Encoder[UnknownFieldSet] = (_: UnknownFieldSet) => Json.Null
}
