package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.{Banner, Native, Video}
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.{BidRequest, BidResponse, NativeResponse}
import com.powerspace.bidswitch._
import com.powerspace.openrtb.bidswitch.bidrequest._
import com.powerspace.openrtb.bidswitch.bidresponse.{BidSwitchBidResponseSerde, BidSwitchBidSerde, BidSwitchNativeResponseSerde}
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json._

/**
  * BidSwitch Serialization and Deserialization Module
  * It uses OpenRTB Serdes underneath and applies extensions Serdes at BidResponse and Bid levels
  */
object BidSwitchSerdeModule extends SerdeModule {

  def nativeRegistry: ExtensionRegistry = ExtensionRegistry()
    .registerExtension[NativeResponse, NativeResponseExt](
    extension = BidswitchProto.responseNativeExt,
    encoder = BidSwitchNativeResponseSerde.nativeExtEncoder,
    decoder = BidSwitchNativeResponseSerde.nativeExtDecoder
  )
    .registerExtension[Native, NativeExt](
    extension = BidswitchProto.requestNativeExt,
    encoder = BidSwitchNativeRequestSerde.nativeExtEncoder,
    decoder = BidSwitchNativeRequestSerde.nativeExtDecoder
  )

  def extensionRegistry: ExtensionRegistry = {
    val bidSerde = new BidSwitchBidSerde(nativeResponseSerde)

    ExtensionRegistry()
      .registerExtension[BidRequest, BidRequestExt](
      extension = BidswitchProto.bidRequestExt,
      encoder = BidSwitchBidRequestSerde.bidRequestExtEncoder,
      decoder = BidSwitchBidRequestSerde.bidRequestExtDecoder
    )
      .registerExtension[BidRequest.Imp, ImpressionExt](
      extension = BidswitchProto.impressionExt,
      encoder = BidSwitchImpressionSerde.impressionExtEncoder,
      decoder = BidSwitchImpressionSerde.impressionExtDecoder
    )
      .registerExtension[BidRequest.User, UserExt](
      extension = BidswitchProto.userExt,
      encoder = BidSwitchUserSerde.userExtEncoder,
      decoder = BidSwitchUserSerde.userExtDecoder
    )

      .registerExtension[Banner, BannerExt](
      extension = BidswitchProto.bannerExt,
      encoder = BidSwitchBannerSerde.bannerExtEncoder,
      decoder = BidSwitchBannerSerde.bannerExtDecoder
    )
      .registerExtension[Video, VideoExt](
      extension = BidswitchProto.videoExt,
      encoder = BidSwitchVideoSerde.videoExtEncoder,
      decoder = BidSwitchVideoSerde.videoExtDecoder
    )
      .registerExtension[Deal, DealExt](
      extension = BidswitchProto.dealExt,
      encoder = BidSwitchDealSerde.dealExtEncoder,
      decoder = BidSwitchDealSerde.dealExtDecoder
    )
      .registerExtension[BidResponse, BidResponseExt](
      extension = BidswitchProto.bidResponseExt,
      encoder = BidSwitchBidResponseSerde.bidResponseExtEncoder,
      decoder = BidSwitchBidResponseSerde.bidResponseExtDecoder
    )
      .registerExtension[Bid, BidExt](
      extension = BidswitchProto.bidExt,
      encoder = bidSerde.bidExtEncoder,
      decoder = bidSerde.bidExtDecoder
    )
  }


}
