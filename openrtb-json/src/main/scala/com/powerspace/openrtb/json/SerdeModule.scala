package com.powerspace.openrtb.json

import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.bidrequest._
import com.powerspace.openrtb.json.bidresponse.{OpenRtbBidResponseSerde, OpenRtbBidSerde, OpenRtbNativeSerde, OpenRtbSeatBidSerde}

trait SerdeModule extends ConfiguredSerde {

  def nativeRegistry: ExtensionRegistry

  def extensionRegistry: ExtensionRegistry

  protected def nativeRequestSerde = new OpenRtbNativeRequestSerde(
    new OpenRtbVideoSerde(
      new OpenRtbBannerSerde()(extensionRegistry))(extensionRegistry))(nativeRegistry)

  protected def impressionLevelSerde = new ImpressionLevelSerdes(
    new OpenRtbVideoSerde(
      new OpenRtbBannerSerde()(extensionRegistry))(extensionRegistry))(extensionRegistry)

  protected def impressionSerde = new OpenRtbImpressionSerde()(extensionRegistry)

  protected def nativeResponseSerde = new OpenRtbNativeSerde()(nativeRegistry)

  protected def bidSerde = new OpenRtbBidSerde(nativeResponseSerde)(extensionRegistry + nativeRegistry)

  protected def seatBidSerde = new OpenRtbSeatBidSerde(bidSerde)(extensionRegistry)

  protected def bidRequestSerde = new OpenRtbBidRequestSerde()(extensionRegistry)

  protected def bidResponseSerde = new OpenRtbBidResponseSerde()(extensionRegistry)

  protected def userSerde = new OpenRtbUserSerde(bidRequestSerde)(extensionRegistry)

  protected def pmpSerde = new OpenRtbPmpSerde()(extensionRegistry)

  implicit private def bannerEncoder = impressionLevelSerde.bannerEncoder

  implicit private def videoEncoder = impressionLevelSerde.videoEncoder

  implicit private def audioEncoder = impressionLevelSerde.audioEncoder

  implicit private def dealEncoder = pmpSerde.dealEncoder

  implicit private def pmpEncoder = impressionLevelSerde.pmpEncoder

  implicit private def nativeRequestEncoder = nativeRequestSerde.encoder

  implicit private def bannerDecoder = impressionLevelSerde.bannerDecoder

  implicit private def videoDecoder = impressionLevelSerde.videoDecoder

  implicit private def audioDecoder = impressionLevelSerde.audioDecoder

  implicit private def dealDecoder = pmpSerde.dealDecoder

  implicit private def pmpDecoder = impressionLevelSerde.pmpDecoder

  implicit private def nativeRequestDecoder = nativeRequestSerde.decoder

  implicit private def userEncoder = userSerde.encoder

  implicit private def userDecoder = userSerde.decoder

  implicit private def impEncoder = impressionSerde.encoder

  implicit private def impDecoder = impressionSerde.decoder

  implicit private def bidEncoder = bidSerde.encoder

  implicit private def seatBidEncoder = seatBidSerde.encoder

  implicit private def bidDecoder = bidSerde.decoder

  implicit private def seatBidDecoder = seatBidSerde.decoder

  implicit def bidRequestEncoder = bidRequestSerde.encoder

  implicit def bidRequestDecoder = bidRequestSerde.decoder

  implicit def bidResponseDecoder = bidResponseSerde.decoder

  implicit def bidResponseEncoder = bidResponseSerde.encoder

  def +(other: SerdeModule) = {
    val us = this
    new SerdeModule {
      override def nativeRegistry: ExtensionRegistry = us.nativeRegistry + other.nativeRegistry
      override def extensionRegistry: ExtensionRegistry = us.extensionRegistry + other.extensionRegistry
    }
  }
}

