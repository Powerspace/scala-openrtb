package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import io.circe.{Decoder, Encoder, Json}

/**
  * Serialize and Deserialize an OpenRTB BidRequest
  */
object BidRequestSerde {

  /**
    * Encoder for the OpenRTB bid request.
    */
  def encoder: Encoder[BidRequest] = Encoder.instance(_ => Json.True)

  /**
    * Decoder for the OpenRTB bid request.
    */
  def decoder: Decoder[BidRequest] = ???

}
