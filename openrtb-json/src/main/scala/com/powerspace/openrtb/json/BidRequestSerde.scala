package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Pmp, Video}
import com.google.openrtb.BidRequest.{Imp, Source}
import com.google.openrtb.NativeRequest.Asset.AssetOneof
import com.google.openrtb.NativeRequest.{Asset, EventTrackers}
import com.google.openrtb._
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration
import io.circe.syntax._
/**
  * Serialize and Deserialize an OpenRTB BidRequest
  */
object BidRequestSerde {

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  // Enums encoding
  implicit val adPositionEncoder: Encoder[AdPosition] = protobufEnumEncoder[AdPosition]
  implicit val adUnitEncoder: Encoder[AdUnitId] = protobufEnumEncoder[AdUnitId]
  implicit val apiFrameworkEncoder: Encoder[APIFramework] = protobufEnumEncoder[APIFramework]
  implicit val auctionTypeEncoder: Encoder[AuctionType] = protobufEnumEncoder[AuctionType]
  implicit val bannerAdTypeEncoder: Encoder[BannerAdType] = protobufEnumEncoder[BannerAdType]
  implicit val companionTypeEncoder: Encoder[CompanionType] = protobufEnumEncoder[CompanionType]
  implicit val connectionTypeEncoder: Encoder[ConnectionType] = protobufEnumEncoder[ConnectionType]
  implicit val contentContextEncoder: Encoder[ContentContext] = protobufEnumEncoder[ContentContext]
  implicit val contentDeliveryMethodEncoder: Encoder[ContentDeliveryMethod] = protobufEnumEncoder[ContentDeliveryMethod]
  implicit val contextSubTypeEncoder: Encoder[ContextSubtype] = protobufEnumEncoder[ContextSubtype]
  implicit val contextTypeEncoder: Encoder[ContextType] = protobufEnumEncoder[ContextType]
  implicit val creativeAttributeEncoder: Encoder[CreativeAttribute] = protobufEnumEncoder[CreativeAttribute]
  implicit val dataAssetTypeEncoder: Encoder[DataAssetType] = protobufEnumEncoder[DataAssetType]
  implicit val deviceTypeEncoder: Encoder[DeviceType] = protobufEnumEncoder[DeviceType]
  implicit val eventTypeEncoder: Encoder[EventType] = protobufEnumEncoder[EventType]
  implicit val eventTrackingMethodEncoder: Encoder[EventTrackingMethod] = protobufEnumEncoder[EventTrackingMethod]
  implicit val expandableDirectionEncoder: Encoder[ExpandableDirection] = protobufEnumEncoder[ExpandableDirection]
  implicit val feedTypeEncoder: Encoder[FeedType] = protobufEnumEncoder[FeedType]
  implicit val imageAssetTypeEncoder: Encoder[ImageAssetType] = protobufEnumEncoder[ImageAssetType]
  implicit val layoutEncoder: Encoder[LayoutId] = protobufEnumEncoder[LayoutId]
  implicit val locationServiceEncoder: Encoder[LocationService] = protobufEnumEncoder[LocationService]
  implicit val locationTypeEncoder: Encoder[LocationType] = protobufEnumEncoder[LocationType]
  implicit val placementTypeEncoder: Encoder[PlacementType] = protobufEnumEncoder[PlacementType]
  implicit val playbackCessationModeEncoder: Encoder[PlaybackCessationMode] = protobufEnumEncoder[PlaybackCessationMode]
  implicit val playbackMethodEncoder: Encoder[PlaybackMethod] = protobufEnumEncoder[PlaybackMethod]
  implicit val productionQualityEncoder: Encoder[ProductionQuality] = protobufEnumEncoder[ProductionQuality]
  implicit val protocolEncoder: Encoder[Protocol] = protobufEnumEncoder[Protocol]
  implicit val qAGMediaRatingContextEncoder: Encoder[QAGMediaRating] = protobufEnumEncoder[QAGMediaRating]
  implicit val videoLinearityEncoder: Encoder[VideoLinearity] = protobufEnumEncoder[VideoLinearity]
  implicit val videoPlacementTypeEncoder: Encoder[VideoPlacementType] = protobufEnumEncoder[VideoPlacementType]
  implicit val volumeNormalizationModeEncoder: Encoder[VolumeNormalizationMode] = protobufEnumEncoder[VolumeNormalizationMode]

  // Imp encoding
  implicit val companionAdEncoder: Encoder[CompanionAd] = deriveEncoder[CompanionAd].transformBooleans.clean
  implicit val formatEncoder: Encoder[Format] = deriveEncoder[Format].transformBooleans.clean
  implicit val bannerEncoder: Encoder[Banner] = deriveEncoder[Banner].transformBooleans.clean
  implicit val videoEncoder: Encoder[Video] =deriveEncoder[Video].transformBooleans.clean
  implicit val audioEncoder: Encoder[Audio] = deriveEncoder[Audio].transformBooleans.clean
  implicit val pmpEncoder: Encoder[Pmp] = deriveEncoder[Pmp].transformBooleans.clean
  implicit val dealEncoder: Encoder[Deal] = deriveEncoder[Deal].transformBooleans.clean

  // Site encoding
  implicit val producerEncoder: Encoder[BidRequest.Producer] = deriveEncoder[BidRequest.Producer].transformBooleans.clean
  implicit val publisherEncoder: Encoder[BidRequest.Publisher] = deriveEncoder[BidRequest.Publisher].transformBooleans.clean
  implicit val contentEncoder: Encoder[BidRequest.Content] = deriveEncoder[BidRequest.Content].transformBooleans.clean

  // Native encoding
  implicit val distChannelOneOfEncoder: Encoder[BidRequest.DistributionchannelOneof] = protobufOneofEncoder[BidRequest.DistributionchannelOneof] {
      case BidRequest.DistributionchannelOneof.App(app) => app.asJson
      case BidRequest.DistributionchannelOneof.Site(site) => site.asJson
    }

  implicit val titleEncoder: Encoder[Asset.Title] = deriveEncoder[Asset.Title].transformBooleans.clean
  implicit val imgEncoder: Encoder[Asset.Image] = deriveEncoder[Asset.Image].transformBooleans.clean
  implicit val assetDataEncoder: Encoder[Asset.Data] = deriveEncoder[Asset.Data].transformBooleans.clean
  implicit val assetOneOfEncoder: Encoder[AssetOneof] = protobufOneofEncoder[AssetOneof]{
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

  // BidRequest encoding
  implicit val segmentEncoder: Encoder[BidRequest.Data.Segment] = deriveEncoder[BidRequest.Data.Segment].transformBooleans.clean
  implicit val dataEncoder: Encoder[BidRequest.Data] = deriveEncoder[BidRequest.Data].transformBooleans.clean
  implicit val geoEncoder: Encoder[BidRequest.Geo] = deriveEncoder[BidRequest.Geo].transformBooleans.clean
  implicit val impEncoder: Encoder[Imp] = deriveEncoder[Imp].transformBooleans.clean
  implicit val metricEncoder: Encoder[Metric] = deriveEncoder[Metric].transformBooleans.clean()
  implicit val sourceEncoder: Encoder[Source] = deriveEncoder[Source].transformBooleans.clean()
  implicit val siteEncoder: Encoder[BidRequest.Site] = deriveEncoder[BidRequest.Site].transformBooleans.clean
  implicit val appEncoder: Encoder[BidRequest.App] = deriveEncoder[BidRequest.App].transformBooleans.clean
  implicit val deviceEncoder: Encoder[BidRequest.Device] = deriveEncoder[BidRequest.Device].transformBooleans.clean
  implicit val userEncoder: Encoder[BidRequest.User] = deriveEncoder[BidRequest.User].transformBooleans.clean
  implicit val regsEncoder: Encoder[BidRequest.Regs] = deriveEncoder[BidRequest.Regs].transformBooleans.clean

  /**
    * Encoder for the OpenRTB bid request.
    */
  def encoder(implicit userEncoder: Encoder[BidRequest.User], impEncoder: Encoder[BidRequest.Imp]): Encoder[BidRequest] = deriveEncoder[BidRequest].transformBooleans.clean(toKeep = Seq("imp"))

  /**
    * Decoder for the OpenRTB bid request.
    */
  def decoder: Decoder[BidRequest] = ???

}
