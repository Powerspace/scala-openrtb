package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import io.circe.{Decoder, Encoder}

object BidRequestSerde {

  implicit val encoder: Encoder[BidRequest] = ???
  implicit val decoder: Decoder[BidRequest] = ???

}
