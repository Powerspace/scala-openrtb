package com.powerspace.openrtb.akka.marshallers

import akka.http.scaladsl.marshalling.Marshaller
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshaller
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.{Decoder, Encoder}

object Marshalling {
  implicit val bidRequestEncoder: Encoder[BidRequest] = OpenRtbSerdeModule.bidRequestEncoder
  implicit val bidResponseDecoder: Decoder[BidResponse] = OpenRtbSerdeModule.bidResponseDecoder

  implicit val marshaller: Marshaller[BidRequest, MessageEntity] = JsonMarshallers.bidRequestJsonMarshaller
  implicit val unmarshaller: Unmarshaller[HttpEntity, BidResponse] = JsonUnmarshallers.bidResponseJsonUnmarshaller
}
