package com.powerspace.openrtb.json

import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import io.circe.Decoder

/**
  * Provides serialization and deserialization for OpenRTB entities by way of Google
  */
class GoogleOpenRtbSerdeModule extends SerdeModule {
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidResponseSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = SeatBidSerDe.decoder
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSerde.decoder
}
