package com.powerspace.openrtb.examples.akka

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller, ToResponseMarshaller}
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, FromRequestUnmarshaller, Unmarshaller}
import akka.stream.Materializer
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.parser.decode
import io.circe.{Decoder, Encoder}

import scala.concurrent.{ExecutionContext, Future}

object Marshalling {
  implicit val bidRequestEncoder: Encoder[BidRequest] = OpenRtbSerdeModule.bidRequestEncoder
  implicit val bidResponseDecoder: Decoder[BidResponse] = OpenRtbSerdeModule.bidResponseDecoder

  implicit val marshaller: Marshaller[BidRequest, MessageEntity] = JsonMarshallers.bidRequestJsonMarshaller
  implicit val unmarshaller: Unmarshaller[HttpEntity, BidResponse] = JsonUnmarshallers.bidResponseJsonUnmarshaller
}

object JsonMarshallers {

  import akka.http.scaladsl.marshalling.Marshaller._

  def bidRequestJsonMarshaller(implicit encoder: Encoder[BidRequest]): ToEntityMarshaller[BidRequest] =
    withFixedContentType(ContentTypes.`application/json`)(
      bidRequest => HttpEntity(MediaTypes.`application/json`, encoder.apply(bidRequest).toString)
    )

  def bidResponseJsonMarshaller(implicit encoder: Encoder[BidResponse]): ToEntityMarshaller[BidResponse] =
    withFixedContentType(ContentTypes.`application/json`)(
      bidResponse => HttpEntity(MediaTypes.`application/json`, encoder.apply(bidResponse).toString)
    )

  def bidRequestHttpJsonMarshaller(implicit encoder: Encoder[BidRequest]): ToResponseMarshaller[BidRequest] =
    withFixedContentType(ContentTypes.`application/json`)(
      bidRequest => HttpResponse()
        .withEntity(HttpEntity(MediaTypes.`application/json`, encoder.apply(bidRequest).toString))
    )

  def bidResponseHttpJsonMarshaller(implicit encoder: Encoder[BidResponse]): ToResponseMarshaller[BidResponse] =
    withFixedContentType(ContentTypes.`application/json`)(
      bidResponse => HttpResponse()
        .withEntity(HttpEntity(MediaTypes.`application/json`, encoder.apply(bidResponse).toString))
    )
}

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
      override def apply(value: HttpRequest)
                        (implicit ec: ExecutionContext, materializer: Materializer): Future[BidRequest] =
        Unmarshaller
          .stringUnmarshaller(value.entity)
          .map(implicit json => decode[BidRequest](json).handlingFailure)
    }

  def bidResponseHttpJsonUnmarshaller(implicit decoder: Decoder[BidResponse]): FromRequestUnmarshaller[BidResponse] =
    new FromRequestUnmarshaller[BidResponse]() {
      override def apply(value: HttpRequest)
                        (implicit ec: ExecutionContext, materializer: Materializer): Future[BidResponse] =
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