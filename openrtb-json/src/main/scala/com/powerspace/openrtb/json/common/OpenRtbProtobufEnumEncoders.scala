package com.powerspace.openrtb.json.common

import com.google.openrtb._
import com.powerspace.openrtb.json.util.EncodingUtils.protobufEnumEncoder
import io.circe.Encoder

/**
  * OpenRTB Protocol Buffer Enumerations Encoders
  */
object OpenRtbProtobufEnumEncoders {

  implicit val companionTypeEncoder: Encoder[CompanionType] = protobufEnumEncoder[CompanionType]
  implicit val apiFrameworkEncoder: Encoder[APIFramework] = protobufEnumEncoder[APIFramework]
  implicit val playbackMethodEncoder: Encoder[PlaybackMethod] = protobufEnumEncoder[PlaybackMethod]
  implicit val videoLinearityEncoder: Encoder[VideoLinearity] = protobufEnumEncoder[VideoLinearity]
  implicit val videoPlacementTypeEncoder: Encoder[VideoPlacementType] = protobufEnumEncoder[VideoPlacementType]
  implicit val protocolEncoder: Encoder[Protocol] = protobufEnumEncoder[Protocol]
  implicit val creativeAttributeEncoder: Encoder[CreativeAttribute] = protobufEnumEncoder[CreativeAttribute]
  implicit val contentDeliveryMethodEncoder: Encoder[ContentDeliveryMethod] = protobufEnumEncoder[ContentDeliveryMethod]
  implicit val playbackCessationModeEncoder: Encoder[PlaybackCessationMode] = protobufEnumEncoder[PlaybackCessationMode]
  implicit val auctionTypeEncoder: Encoder[AuctionType] = protobufEnumEncoder[AuctionType]
  implicit val volumeNormalizationModeEncoder: Encoder[VolumeNormalizationMode] =
    protobufEnumEncoder[VolumeNormalizationMode]
  implicit val bannerAdTypeEncoder: Encoder[BannerAdType] = protobufEnumEncoder[BannerAdType]
  implicit val adPositionEncoder: Encoder[AdPosition] = protobufEnumEncoder[AdPosition]
  implicit val expandableDirectionEncoder: Encoder[ExpandableDirection] = protobufEnumEncoder[ExpandableDirection]
  implicit val adUnitEncoder: Encoder[AdUnitId] = protobufEnumEncoder[AdUnitId]
  implicit val connectionTypeEncoder: Encoder[ConnectionType] = protobufEnumEncoder[ConnectionType]
  implicit val contentContextEncoder: Encoder[ContentContext] = protobufEnumEncoder[ContentContext]
  implicit val contextSubTypeEncoder: Encoder[ContextSubtype] = protobufEnumEncoder[ContextSubtype]
  implicit val contextTypeEncoder: Encoder[ContextType] = protobufEnumEncoder[ContextType]
  implicit val dataAssetTypeEncoder: Encoder[DataAssetType] = protobufEnumEncoder[DataAssetType]
  implicit val deviceTypeEncoder: Encoder[DeviceType] = protobufEnumEncoder[DeviceType]
  implicit val eventTypeEncoder: Encoder[EventType] = protobufEnumEncoder[EventType]
  implicit val eventTrackingMethodEncoder: Encoder[EventTrackingMethod] = protobufEnumEncoder[EventTrackingMethod]
  implicit val feedTypeEncoder: Encoder[FeedType] = protobufEnumEncoder[FeedType]
  implicit val imageAssetTypeEncoder: Encoder[ImageAssetType] = protobufEnumEncoder[ImageAssetType]
  implicit val layoutEncoder: Encoder[LayoutId] = protobufEnumEncoder[LayoutId]
  implicit val locationServiceEncoder: Encoder[LocationService] = protobufEnumEncoder[LocationService]
  implicit val locationTypeEncoder: Encoder[LocationType] = protobufEnumEncoder[LocationType]
  implicit val placementTypeEncoder: Encoder[PlacementType] = protobufEnumEncoder[PlacementType]
  implicit val productionQualityEncoder: Encoder[ProductionQuality] = protobufEnumEncoder[ProductionQuality]
  implicit val qAGMediaRatingContextEncoder: Encoder[QAGMediaRating] = protobufEnumEncoder[QAGMediaRating]
  implicit val noBidReasonEncoder: Encoder[NoBidReason] = protobufEnumEncoder[NoBidReason]

}
