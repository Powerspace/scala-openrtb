package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import io.circe.{Decoder, Encoder}

object BidRequestSerde {
  implicit val decoder: Decoder[BidRequest] = ???
}
