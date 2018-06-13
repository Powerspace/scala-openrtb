package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.NativeResponse.Asset.AssetOneof
import com.google.openrtb.NativeResponse.Asset.AssetOneof.{Data, Img, Title, Video}
import com.google.openrtb.NativeResponse.{Asset, EventTracker, Link}
import com.google.openrtb._
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB Native Serde
  */
object OpenRtbNativeSerde {

  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * Decoder for the OpenRTB link object.
    */
  private implicit val linkDecoder: Decoder[NativeResponse.Link] =
    cursor => for {
      url <- cursor.downField("url").as[String]
      clicktrackers <- cursor.downField("clicktrackers").as[Option[Seq[String]]]
      fallback <- cursor.downField("fallback").as[Option[String]]
    } yield NativeResponse.Link(url = url, clicktrackers = clicktrackers.getOrElse(Seq()), fallback = fallback)

  /**
    * Decoder for the OpenRTB title asset object.
    */
  private implicit val titleDecoder: Decoder[NativeResponse.Asset.Title] =
    cursor => for {
      text <- cursor.downField("text").as[String]
      len <- cursor.downField("len").as[Option[Int]]
    } yield NativeResponse.Asset.Title(text = text, len = len)

  /**
    * Decoder for the OpenRTB image asset object.
    */
  private implicit val imgDecoder: Decoder[NativeResponse.Asset.Image] =
    cursor => for {
      imageType <- cursor.downField("type").as[Option[Int]].map(_.map(ImageAssetType.fromValue))
      url <- cursor.downField("url").as[String]
      w <- cursor.downField("w").as[Option[Int]]
      h <- cursor.downField("h").as[Option[Int]]
    } yield NativeResponse.Asset.Image(`type` = imageType, url = url, w = w, h = h)

  /**
    * Decoder for the OpenRTB video asset object.
    */
  private implicit val videoDecoder: Decoder[NativeResponse.Asset.Video] =
    cursor => for {
      vasttag <- cursor.downField("vasttag").as[String]
    } yield NativeResponse.Asset.Video(vasttag = vasttag)

  /**
    * Decoder for the OpenRTB data asset object.
    */
  private implicit val dataDecoder: Decoder[NativeResponse.Asset.Data] =
    cursor => for {
      dataType <- cursor.downField("type").as[Option[Int]].map(_.map(DataAssetType.fromValue))
      len <- cursor.downField("len").as[Option[Int]]
      label <- cursor.downField("label").as[Option[String]]
      value <- cursor.downField("value").as[String]
    } yield NativeResponse.Asset.Data(`type` = dataType,len = len, label = label, value = value)

  /**
    * Decoder for the OpenRTB eventTracker object.
    */
  private implicit val eventTrackerDecoder: Decoder[NativeResponse.EventTracker] =
    cursor => for {
      event <- cursor.downField("event").as[Option[Int]].map(_.map(EventType.fromValue))
      method <- cursor.downField("event").as[Int].map(EventTrackingMethod.fromValue)
      url <- cursor.downField("url").as[Option[String]]
    } yield NativeResponse.EventTracker(event = event, method = method, url = url)

  /**
    * Decoder for the OpenRTB asset object.
    */
  private implicit val assetDecoder: Decoder[NativeResponse.Asset] =
    cursor => for {
      id <- cursor.downField("id").as[Option[Int]]
      required <- cursor.downField("required").as[Option[Int]]
        .map(_.map(value => if (value < 1) false else true))
      link <- cursor.downField("link").as[Option[NativeResponse.Link]]
      title <- cursor.downField("title").as[Option[NativeResponse.Asset.Title]].map(_.map(AssetOneof.Title))
      img <- cursor.downField("img").as[Option[NativeResponse.Asset.Image]].map(_.map(AssetOneof.Img))
      video <- cursor.downField("video").as[Option[NativeResponse.Asset.Video]].map(_.map(AssetOneof.Video))
      data <- cursor.downField("data").as[Option[NativeResponse.Asset.Data]].map(_.map(AssetOneof.Data))
      assetOneOf = title
        .orElse(img)
        .orElse(video)
        .orElse(data)
        .getOrElse(AssetOneof.Empty)
  } yield NativeResponse.Asset(id = id.getOrElse(0), required = required, link = link, assetOneof = assetOneOf)

  /**
    * Decoder for the OpenRTB native object.
    */
  def decoder: Decoder[NativeResponse] =
    cursor => for {
      ver <- cursor.downField("ver").as[Option[String]]
      assets <- cursor.downField("assets").as[Seq[NativeResponse.Asset]]
      assetsurl <- cursor.downField("assetsurl").as[Option[String]]
      dcourl <- cursor.downField("dcourl").as[Option[String]]
      link <- cursor.downField("link").as[NativeResponse.Link]
      imptrackers <- cursor.downField("imptrackers").as[Option[Seq[String]]]
      jstracker <- cursor.downField("jstracker").as[Option[String]]
      eventtrackers <- cursor.downField("eventtrackers").as[Option[Seq[NativeResponse.EventTracker]]]
      privacy <- cursor.downField("privacy").as[Option[String]]
    } yield {
      NativeResponse(ver = ver, assets = assets, assetsurl = assetsurl, dcourl = dcourl, link = link,
        imptrackers = imptrackers.getOrElse(Seq()), jstracker = jstracker, eventtrackers = eventtrackers.getOrElse(Seq()), privacy = privacy)
    }


  implicit val dataEncoder: Encoder[Asset.Data] = deriveEncoder[Asset.Data].cleanRtb
  implicit val imgEncoder: Encoder[Asset.Image] = deriveEncoder[Asset.Image].cleanRtb
  implicit val titleEncoder: Encoder[Asset.Title] = deriveEncoder[Asset.Title].cleanRtb
  implicit val videoEncoder: Encoder[Asset.Video] = deriveEncoder[Asset.Video].cleanRtb

  implicit val oneofDataEncoder: Encoder[AssetOneof.Data] = deriveEncoder[Data].cleanRtb
  implicit val oneofImgEncoder: Encoder[AssetOneof.Img] = deriveEncoder[Img].cleanRtb
  implicit val oneofTitleEncoder: Encoder[AssetOneof.Title] = deriveEncoder[Title].cleanRtb
  implicit val oneofVideoEncoder: Encoder[AssetOneof.Video] = deriveEncoder[Video].cleanRtb

  implicit val assetsOneofEncoder: Encoder[Asset.AssetOneof] = protobufOneofEncoder[Asset.AssetOneof] {
    case Asset.AssetOneof.Data(data) => data.asJson
    case Asset.AssetOneof.Img(img) => img.asJson
    case Asset.AssetOneof.Title(title) => title.asJson
    case Asset.AssetOneof.Video(video) => video.asJson
  }

  implicit val linkEncoder: Encoder[Link] = deriveEncoder[Link].cleanRtb

  // @todo make it manual
  implicit val assetsEncoder: Encoder[Asset] = deriveEncoder[Asset].cleanRtb

  implicit val eventTrackerEncoder: Encoder[EventTracker] = deriveEncoder[EventTracker].cleanRtb
  implicit val nativeResponseEncoder: Encoder[NativeResponse] = deriveEncoder[NativeResponse].cleanRtb

}