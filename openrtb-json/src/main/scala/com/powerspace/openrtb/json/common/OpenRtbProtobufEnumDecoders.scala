package com.powerspace.openrtb.json.common

import com.google.openrtb._
import io.circe._
import com.powerspace.openrtb.json.util.EncodingUtils.protobufEnumDecoder

/**
  * OpenRTB Protocol Buffer Enumerations Decoders
  */
object OpenRtbProtobufEnumDecoders {

  implicit val companionTypeDecoder: Decoder[CompanionType] = protobufEnumDecoder[CompanionType]
  implicit val apiFrameworkDecoder: Decoder[APIFramework] = protobufEnumDecoder[APIFramework]
  implicit val playbackMethodDecoder: Decoder[PlaybackMethod] = protobufEnumDecoder[PlaybackMethod]
  implicit val videoLinearityDecoder: Decoder[VideoLinearity] = protobufEnumDecoder[VideoLinearity]
  implicit val videoPlacementTypeDecoder: Decoder[VideoPlacementType] = protobufEnumDecoder[VideoPlacementType]
  implicit val protocolDecoder: Decoder[Protocol] = protobufEnumDecoder[Protocol]
  implicit val creativeAttributeDecoder: Decoder[CreativeAttribute] = protobufEnumDecoder[CreativeAttribute]
  implicit val contentDeliveryMethodDecoder: Decoder[ContentDeliveryMethod] = protobufEnumDecoder[ContentDeliveryMethod]
  implicit val playbackCessationModeDecoder: Decoder[PlaybackCessationMode] = protobufEnumDecoder[PlaybackCessationMode]
  implicit val auctionTypeDecoder: Decoder[AuctionType] = protobufEnumDecoder[AuctionType]
  implicit val volumeNormalizationModeDecoder: Decoder[VolumeNormalizationMode] =
    protobufEnumDecoder[VolumeNormalizationMode]
  implicit val bannerAdTypeDecoder: Decoder[BannerAdType] = protobufEnumDecoder[BannerAdType]
  implicit val adPositionDecoder: Decoder[AdPosition] = protobufEnumDecoder[AdPosition]
  implicit val expandableDirectionDecoder: Decoder[ExpandableDirection] = protobufEnumDecoder[ExpandableDirection]
  implicit val adUnitDecoder: Decoder[AdUnitId] = protobufEnumDecoder[AdUnitId]
  implicit val connectionTypeDecoder: Decoder[ConnectionType] = protobufEnumDecoder[ConnectionType]
  implicit val contentContextDecoder: Decoder[ContentContext] = protobufEnumDecoder[ContentContext]
  implicit val contextSubTypeDecoder: Decoder[ContextSubtype] = protobufEnumDecoder[ContextSubtype]
  implicit val contextTypeDecoder: Decoder[ContextType] = protobufEnumDecoder[ContextType]
  implicit val dataAssetTypeDecoder: Decoder[DataAssetType] = protobufEnumDecoder[DataAssetType]
  implicit val deviceTypeDecoder: Decoder[DeviceType] = protobufEnumDecoder[DeviceType]
  implicit val eventTypeDecoder: Decoder[EventType] = protobufEnumDecoder[EventType]
  implicit val eventTrackingMethodDecoder: Decoder[EventTrackingMethod] = protobufEnumDecoder[EventTrackingMethod]
  implicit val feedTypeDecoder: Decoder[FeedType] = protobufEnumDecoder[FeedType]
  implicit val imageAssetTypeDecoder: Decoder[ImageAssetType] = protobufEnumDecoder[ImageAssetType]
  implicit val layoutDecoder: Decoder[LayoutId] = protobufEnumDecoder[LayoutId]
  implicit val locationServiceDecoder: Decoder[LocationService] = protobufEnumDecoder[LocationService]
  implicit val locationTypeDecoder: Decoder[LocationType] = protobufEnumDecoder[LocationType]
  implicit val placementTypeDecoder: Decoder[PlacementType] = protobufEnumDecoder[PlacementType]
  implicit val productionQualityDecoder: Decoder[ProductionQuality] = protobufEnumDecoder[ProductionQuality]
  implicit val qAGMediaRatingContextDecoder: Decoder[QAGMediaRating] = protobufEnumDecoder[QAGMediaRating]
  implicit val noBidReasonEncoder: Decoder[NoBidReason] = protobufEnumDecoder[NoBidReason]

}
