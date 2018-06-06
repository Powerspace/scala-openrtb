package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp
import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.json.bidrequest.BidRequestSerde
import com.powerspace.openrtb.json.bidresponse.{BidResponseSerde, BidSerde, SeatBidSerde}
import io.circe.{Decoder, Encoder}

/**
  * Provides serialization and deserialization for OpenRTB entities.
  */
object OpenRtbSerdeModule extends SerdeModule {

  // bid request encoding
  override implicit val nativeEncoder: Encoder[Imp.Native] = BidRequestSerde.nativeEncoder
  override implicit val bannerEncoder: Encoder[Imp.Banner] = BidRequestSerde.bannerEncoder
  override implicit val dealEncoder: Encoder[Pmp.Deal] = BidRequestSerde.dealEncoder
  override implicit val videoEncoder: Encoder[Imp.Video] = BidRequestSerde.videoEncoder
  override implicit val userEncoder: Encoder[BidRequest.User] = BidRequestSerde.userEncoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = BidRequestSerde.impEncoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = BidRequestSerde.encoder

  // bid response decoding
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = SeatBidSerde.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidResponseSerde.decoder

}
