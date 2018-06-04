package com.powerspace.openrtb.json

import com.google.openrtb.{BidRequest, BidResponse}
import io.circe.{Decoder, Encoder}

trait SerdeModule {
  implicit val bidResponseDecoder: Decoder[BidResponse]
  implicit val seatBidDecoder: Decoder[BidResponse.SeatBid]
  implicit val bidDecoder: Decoder[BidResponse.SeatBid.Bid]
  implicit val bidRequestEncoder: Encoder[BidRequest]
}

