package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import io.circe.{Decoder, Encoder, Json}

/**
  * Serialize and Deserialize an OpenRTB BidRequest
  */
object BidRequestSerde {
  def encoder: Encoder[BidRequest] = Encoder.instance(_ => Json.True)
  def decoder: Decoder[BidRequest] = ???
}
