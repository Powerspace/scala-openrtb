package com.powerspace.openrtb.json

import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import io.circe.Decoder

class PlainGoogleLikeSerdeModule extends SerdeModule {
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = SeatBidSerDe.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidResponseSerde.decoder
}
