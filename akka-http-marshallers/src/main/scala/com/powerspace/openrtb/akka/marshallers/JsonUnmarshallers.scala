package com.powerspace.openrtb.akka.marshallers

import akka.http.scaladsl.model.{ContentTypeRange, ContentTypes, HttpRequest}
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, Unmarshaller}
import akka.stream.Materializer
import com.google.openrtb.{BidRequest, BidResponse}
import io.circe.Decoder

import scala.concurrent.{ExecutionContext, Future}
import io.circe.parser.decode

object JsonUnmarshallers {

  import akka.http.scaladsl.unmarshalling.PredefinedFromEntityUnmarshallers._

  def bidRequestJsonUnmarshaller(implicit decoder: Decoder[BidRequest]): FromEntityUnmarshaller[BidRequest] =
    stringUnmarshaller
      .forContentTypes(ContentTypeRange(ContentTypes.`application/json`))
      .map(implicit json => decode[BidRequest](json).handlingFailure)

  def bidResponseJsonUnmarshaller(implicit decoder: Decoder[BidResponse]): FromEntityUnmarshaller[BidResponse] =
    stringUnmarshaller
      .forContentTypes(ContentTypeRange(ContentTypes.`application/json`))
      .map(implicit json => decode[BidResponse](json).handlingFailure)

  def bidRequestHttpJsonUnmarshaller(implicit decoder: Decoder[BidRequest]): FromRequestUnmarshaller[BidRequest] =
    new FromRequestUnmarshaller[BidRequest]() {
      override def apply(
        value: HttpRequest)(implicit ec: ExecutionContext, materializer: Materializer): Future[BidRequest] =
        Unmarshaller
          .stringUnmarshaller(value.entity)
          .map(implicit json => decode[BidRequest](json).handlingFailure)
    }

  def bidResponseHttpJsonUnmarshaller(implicit decoder: Decoder[BidResponse]): FromRequestUnmarshaller[BidResponse] =
    new FromRequestUnmarshaller[BidResponse]() {
      override def apply(
        value: HttpRequest)(implicit ec: ExecutionContext, materializer: Materializer): Future[BidResponse] =
        Unmarshaller
          .stringUnmarshaller(value.entity)
          .map(implicit json => decode[BidResponse](json).handlingFailure)
    }

  implicit class DecodingResultEnhancement[T](decodingResult: Either[_ <: Exception, T]) {

    def handlingFailure(implicit json: String): T = decodingResult match {
      case Right(decoded) => decoded
      case Left(error) => throw new UnsupportedOperationException(error)
    }
  }

}
