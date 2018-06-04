package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp.Banner.Format
import com.google.openrtb.BidRequest.Imp.Pmp.Deal
import com.google.openrtb.BidRequest.Imp.Video.CompanionAd
import com.google.openrtb._
import com.google.openrtb.BidRequest.Imp.{Audio, Banner, Metric, Native, Pmp, Video}
import com.google.openrtb.BidRequest.{Imp, Source}
import com.powerspace.openrtb.json.util.JsonUtils
import io.circe.generic.extras.Configuration



/**
  * Serialize and Deserialize an OpenRTB BidRequest
  */
object BidRequestSerde {
  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._

  import JsonUtils._
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val bannerAdTypeEncoder: Encoder[BannerAdType] = protobufEnumEncoder[BannerAdType]
  implicit val creativeAttributeEncoder: Encoder[CreativeAttribute] = protobufEnumEncoder[CreativeAttribute]
  implicit val adPositionEncoder: Encoder[AdPosition] = protobufEnumEncoder[AdPosition]
  implicit val expandableDirectionEncoder: Encoder[ExpandableDirection] = protobufEnumEncoder[ExpandableDirection]
  implicit val apiFrameworkEncoder: Encoder[APIFramework] = protobufEnumEncoder[APIFramework]

  implicit val protocolEncoder: Encoder[Protocol] = protobufEnumEncoder[Protocol]
  implicit val videoPlacementTypeEncoder: Encoder[VideoPlacementType] = protobufEnumEncoder[VideoPlacementType]
  implicit val videoLinearityEncoder: Encoder[VideoLinearity] = protobufEnumEncoder[VideoLinearity]
  implicit val playbackMethodEncoder: Encoder[PlaybackMethod] = protobufEnumEncoder[PlaybackMethod]
  implicit val playbackCessationModeEncoder: Encoder[PlaybackCessationMode] = protobufEnumEncoder[PlaybackCessationMode]
  implicit val contentDeliveryMethodEncoder: Encoder[ContentDeliveryMethod] = protobufEnumEncoder[ContentDeliveryMethod]
  implicit val companionTypeEncoder: Encoder[CompanionType] = protobufEnumEncoder[CompanionType]

  implicit val feedTypeEncoder: Encoder[FeedType] = protobufEnumEncoder[FeedType]
  implicit val volumeNormalizationModeEncoder: Encoder[VolumeNormalizationMode] = protobufEnumEncoder[VolumeNormalizationMode]
  implicit val auctionTypeEncoder: Encoder[AuctionType] = protobufEnumEncoder[AuctionType]

  implicit val companionAdEncoder: Encoder[CompanionAd] = deriveEncoder[CompanionAd].clean
  implicit val formatEncoder: Encoder[Format] = deriveEncoder[Format].clean
  implicit val bannerEncoder: Encoder[Banner] = deriveEncoder[Banner].recodeBooleans.clean
  implicit val videoEncoder: Encoder[Video] =deriveEncoder[Video].recodeBooleans.clean
  implicit val sourceEncoder: Encoder[Source] = deriveEncoder[Source].clean()
  implicit val audioEncoder: Encoder[Audio] = deriveEncoder[Audio].recodeBooleans.clean
  implicit val pmpEncoder: Encoder[Pmp] = deriveEncoder[Pmp].recodeBooleans.clean
  implicit val dealEncoder: Encoder[Deal] = deriveEncoder[Deal].recodeBooleans.clean

  /**
    * Encoder for the OpenRTB native object.
    */
  implicit val nativeEncoder: Encoder[Native] =
    NoNullsEncoder(native => Seq(
      //("request", native.requestOneof.value.asJson), //@todo: to be verified
      ("ver", native.ver.asJson),
      ("api", native.api.map(_.value).asJson),
      ("battr", native.battr.map(_.value).asJson)
    ))

  implicit val metricEncoder: Encoder[Metric] = deriveEncoder[Metric].clean()

  implicit val impEncoder: Encoder[Imp] = NoNullsEncoder(imp => {
    Seq(
      ("id", imp.id.asJson),
      ("metric", imp.metric.asJson),
      ("banner", imp.banner.asJson),
      ("video", imp.video.asJson),
      ("audio", imp.audio.asJson),
      ("native", imp.native.asJson),
      ("pmp", imp.pmp.asJson),
      ("displaymanager", imp.displaymanager.asJson),
      ("displaymanagerver", imp.displaymanagerver.asJson),
      ("instl", imp.instl.map(_.toInt).asJson),
      ("tagid", imp.tagid.asJson),
      ("bidfloor", imp.bidfloor.asJson),
      ("bidfloorcur", imp.bidfloorcur.asJson),
      ("clickbrowser", imp.clickbrowser.map(_.toInt).asJson),
      ("secure", imp.secure.map(_.toInt).asJson),
      ("iframebuster", imp.iframebuster.asJson),
      ("exp", imp.exp.asJson)
    )
  })

  /**
    * Encoder for the OpenRTB bid request.
    */
  def encoder: Encoder[BidRequest] = NoNullsEncoder(request =>
    Seq(
      ("id", request.id.asJson),
      ("imp", request.imp.asJson),
      //("site", ???),
      //("app", ???),
      //("device", ???),
      //("user", ???),
      ("test", request.test.map(_.toInt).asJson),
      ("at", request.at.map(_.value).asJson),
      ("tmax", request.tmax.asJson),
      ("wseat", request.wseat.asJson),
      ("bseat", request.bseat.asJson),
      ("allimps", request.bseat.asJson),
      ("cur", request.cur.asJson),
      ("wlang", request.wlang.asJson),
      ("bcat", request.bcat.asJson),
      ("badv", request.badv.asJson),
      ("bapp", request.bapp.asJson),
      ("source", request.source.asJson)
      //("regs", ???),
    ), toKeep = Seq("imp"))

  /**
    * Decoder for the OpenRTB bid request.
    */
  def decoder: Decoder[BidRequest] = ???

}
