package com.powerspace.openrtb.json

import com.google.openrtb.BidResponse
import io.circe.Decoder

trait SerdeModule {
  implicit val bidResponseDecoder: Decoder[BidResponse]
  implicit val seatBidDecoder: Decoder[BidResponse.SeatBid]
  implicit val bidDecoder: Decoder[BidResponse.SeatBid.Bid]
}

