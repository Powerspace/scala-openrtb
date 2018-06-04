package com.powerspace.openrtb.json

import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import io.circe.{Decoder, Encoder}

/**
  * Provides serialization and deserialization for OpenRTB entities.
  */
object OpenRtbSerdeModule extends SerdeModule {
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = SeatBidSerDe.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidResponseSerde.decoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = BidRequestSerde.encoder
}
