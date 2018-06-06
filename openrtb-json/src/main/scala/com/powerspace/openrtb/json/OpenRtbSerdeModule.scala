package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp
import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.json.bidrequest._
import com.powerspace.openrtb.json.bidresponse.{OpenRtbBidResponseSerde, OpenRtbBidSerde, OpenRtbSeatBidSerde}
import io.circe.{Decoder, Encoder}

/**
  * Provides serialization and deserialization for OpenRTB entities.
  */
object OpenRtbSerdeModule extends SerdeModule {

  // bid request encoding
  implicit val nativeEncoder: Encoder[Imp.Native] = OpenRtbImpressionSerde.nativeEncoder
  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.bannerEncoder
  implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.videoEncoder
  implicit val pmpEncoder: Encoder[Pmp] = OpenRtbPmpSerde.pmpEncoder //@todo make the custom using deal serde

  override implicit val userEncoder: Encoder[BidRequest.User] = OpenRtbUserSerde.userEncoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = OpenRtbImpressionSerde.encoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = OpenRtbBidRequestSerde.encoder

  // bid response decoding
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = OpenRtbBidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = OpenRtbSeatBidSerde.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = OpenRtbBidResponseSerde.decoder

}
