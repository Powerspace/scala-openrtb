package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import io.circe.{Decoder, Encoder}

/**
  * Serialize and Deserialize an OpenRTB BidRequest
  */
object BidRequestSerde {
  def encoder: Encoder[BidRequest] = ???
  def decoder: Decoder[BidRequest] = ???
}
