package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb._
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.NativeRequest.{Asset, EventTrackers}
import com.google.openrtb.NativeRequest.Asset.AssetOneof
import com.powerspace.openrtb.json.util.EncodingUtils

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

  implicit val titleEncoder: Encoder[Asset.Title] = deriveEncoder[Asset.Title].transformBooleans.clean
  implicit val imgEncoder: Encoder[Asset.Image] = deriveEncoder[Asset.Image].transformBooleans.clean
  implicit val assetDataEncoder: Encoder[Asset.Data] = deriveEncoder[Asset.Data].transformBooleans.clean

  implicit val assetOneOfEncoder: Encoder[AssetOneof] = protobufOneofEncoder[AssetOneof] {
    case AssetOneof.Img(img) => img.asJson
    case AssetOneof.Data(data) => data.asJson
    case AssetOneof.Video(video) => video.asJson
    case AssetOneof.Title(title) => title.asJson
  }

  implicit val assetEncoder: Encoder[Asset] = deriveEncoder[Asset].transformBooleans.clean
  implicit val eventTrackersEncoder: Encoder[EventTrackers] = deriveEncoder[EventTrackers].transformBooleans.clean
  implicit val nativeRequestEncoder: Encoder[NativeRequest] = deriveEncoder[NativeRequest].transformBooleans.clean

  implicit val requestOneOfEncoder: Encoder[RequestOneof] = protobufOneofEncoder[Imp.Native.RequestOneof] {
    case RequestOneof.Request(string) => string.asJson
    case RequestOneof.RequestNative(request) => request.asJson
  }

  implicit val nativeEncoder: Encoder[Imp.Native] = deriveEncoder[Imp.Native].transformBooleans.clean

  implicit val metricEncoder: Encoder[Imp.Metric] = deriveEncoder[Imp.Metric].transformBooleans.clean()
  implicit val audioEncoder: Encoder[Imp.Audio] = deriveEncoder[Imp.Audio].transformBooleans.clean

  implicit def encoder(implicit nativeEncoder: Encoder[Imp.Native], bannerEncoder: Encoder[Imp.Banner],
    videoEncoder: Encoder[Imp.Video], pmpEncoder: Encoder[Imp.Pmp]):
    Encoder[Imp] = deriveEncoder[Imp].transformBooleans.clean

}
