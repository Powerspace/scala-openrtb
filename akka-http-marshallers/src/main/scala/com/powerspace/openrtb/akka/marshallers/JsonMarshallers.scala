package com.powerspace.openrtb.akka.marshallers

import akka.http.scaladsl.marshalling.{ToEntityMarshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes}
import com.google.openrtb.{BidRequest, BidResponse}
import io.circe.Encoder

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
      bidRequest =>
        HttpResponse()
          .withEntity(HttpEntity(MediaTypes.`application/json`, encoder.apply(bidRequest).toString))
    )

  def bidResponseHttpJsonMarshaller(implicit encoder: Encoder[BidResponse]): ToResponseMarshaller[BidResponse] =
    withFixedContentType(ContentTypes.`application/json`)(
      bidResponse =>
        HttpResponse()
          .withEntity(HttpEntity(MediaTypes.`application/json`, encoder.apply(bidResponse).toString))
    )
}
