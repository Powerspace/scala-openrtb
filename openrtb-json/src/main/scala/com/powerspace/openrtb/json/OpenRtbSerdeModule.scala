package com.powerspace.openrtb.json

import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.json.bidrequest._
import com.powerspace.openrtb.json.bidresponse.{OpenRtbBidResponseSerde, OpenRtbBidSerde, OpenRtbSeatBidSerde}
import io.circe.{Decoder, Encoder}

/**
  * Provides serialization and deserialization for OpenRTB entities.
  * @todo find a way to move out impression implicits away from here
  */
object OpenRtbSerdeModule extends SerdeModule {

  // bid request encoding
  override implicit val userEncoder: Encoder[BidRequest.User] = OpenRtbUserSerde.encoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = OpenRtbImpressionSerde.encoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = OpenRtbBidRequestSerde.encoder

  // bid response encoding
  override implicit val bidEncoder: Encoder[SeatBid.Bid] = OpenRtbBidSerde.encoder
  override implicit val seatBidEncoder: Encoder[SeatBid] = OpenRtbSeatBidSerde.encoder
  override implicit val bidResponseEncoder: Encoder[BidResponse] = OpenRtbBidResponseSerde.encoder

  // bid response decoding
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = OpenRtbBidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = OpenRtbSeatBidSerde.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = OpenRtbBidResponseSerde.decoder

}
