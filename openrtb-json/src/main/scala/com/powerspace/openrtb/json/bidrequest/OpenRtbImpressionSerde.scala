package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb._
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.NativeRequest.{Asset, EventTrackers}
import com.google.openrtb.NativeRequest.Asset.AssetOneof
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

/**
  * OpenRTB Imp Serde
  */
object OpenRtbImpressionSerde {

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._

  implicit val bannerEncoder: Encoder[Imp.Banner] = OpenRtbBannerSerde.bannerEncoder
  implicit val siteEncoder: Encoder[BidRequest.Site] = OpenRtbBidRequestSerde.siteEncoder
  implicit val appEncoder: Encoder[BidRequest.App] = OpenRtbBidRequestSerde.appEncoder
  implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.videoEncoder

  implicit val distChannelOneOfEncoder: Encoder[BidRequest.DistributionchannelOneof] = protobufOneofEncoder[BidRequest.DistributionchannelOneof] {
    case BidRequest.DistributionchannelOneof.App(app) => app.asJson
    case BidRequest.DistributionchannelOneof.Site(site) => site.asJson
  }

  implicit val titleEncoder: Encoder[Asset.Title] = deriveEncoder[Asset.Title].cleanRtb
  implicit val imgEncoder: Encoder[Asset.Image] = deriveEncoder[Asset.Image].cleanRtb
  implicit val assetDataEncoder: Encoder[Asset.Data] = deriveEncoder[Asset.Data].cleanRtb

  implicit val assetOneOfEncoder: Encoder[AssetOneof] = protobufOneofEncoder[AssetOneof] {
    case AssetOneof.Img(img) => img.asJson
    case AssetOneof.Data(data) => data.asJson
    case AssetOneof.Video(video) => video.asJson
    case AssetOneof.Title(title) => title.asJson
  }
  // @todo name the property accordingly
  implicit val assetEncoder: Encoder[Asset] = deriveEncoder[Asset].cleanRtb
  implicit val eventTrackersEncoder: Encoder[EventTrackers] = deriveEncoder[EventTrackers].cleanRtb
  implicit val nativeRequestEncoder: Encoder[NativeRequest] = deriveEncoder[NativeRequest].cleanRtb

  implicit val requestOneOfEncoder: Encoder[RequestOneof] = protobufOneofEncoder[Imp.Native.RequestOneof] {
    case RequestOneof.Request(string) => string.asJson
    case RequestOneof.RequestNative(request) => request.asJson
  }

  implicit val nativeEncoder: Encoder[Imp.Native] = deriveEncoder[Imp.Native].cleanRtb
  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].cleanRtb
  implicit val audioEncoder: Encoder[Imp.Audio] = deriveEncoder[Imp.Audio].cleanRtb

  implicit def encoder(implicit nativeEncoder: Encoder[Imp.Native], bannerEncoder: Encoder[Imp.Banner],
    videoEncoder: Encoder[Imp.Video], pmpEncoder: Encoder[Imp.Pmp]):
    Encoder[Imp] = deriveEncoder[Imp].cleanRtb

}
