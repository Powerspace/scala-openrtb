package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Pmp
import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.bidswitch.bidrequest._
import com.powerspace.openrtb.bidswitch.bidresponse.{BidSwitchBidDecoder, BidSwitchBidResponseDecoder}
import com.powerspace.openrtb.json._
import io.circe.generic.extras.Configuration

/**
  * BidSwitch Serialization and Deserialization Module
  * It uses OpenRTB Serdes underneath and applies extensions Serdes at BidResponse and Bid levels
  */
object BidSwitchSerdeModule extends SerdeModule {

  import io.circe._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * BidSwitch bid response decoders
    */
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSwitchBidDecoder.bidDecoder
  override implicit val seatBidDecoder: Decoder[SeatBid] = BidSwitchBidDecoder.seatBidDecoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidSwitchBidResponseDecoder.decoder

  /**
    * BidSwitch bid request encoders
    */
  override implicit val nativeEncoder: Encoder[Imp.Native] = BidSwitchNativeEncoder.encoder
  override implicit val bannerEncoder: Encoder[Imp.Banner] = BidSwitchBannerEncoder.encoder
  override implicit val dealEncoder: Encoder[Pmp.Deal] = BidSwitchDealEncoder.encoder
  override implicit val videoEncoder: Encoder[Imp.Video] = BidSwitchVideoEncoder.encoder
  override implicit val userEncoder: Encoder[BidRequest.User] = BidSwitchUserSerde.encoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = BidSwitchImpressionSerde.encoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = BidSwitchBidRequestSerde.encoder

}
