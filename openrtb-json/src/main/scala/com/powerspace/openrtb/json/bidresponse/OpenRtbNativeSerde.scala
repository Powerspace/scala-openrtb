package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.NativeResponse.Asset.AssetOneof
import com.google.openrtb.NativeResponse.Asset.AssetOneof.{Data, Img, Title, Video}
import com.google.openrtb.NativeResponse.{Asset, EventTracker, Link}
import com.google.openrtb._
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB Native Encoder and Decoder
  * @todo split up decoder and encoder
  */
class OpenRtbNativeSerde(implicit er: ExtensionRegistry) extends EncoderProvider[NativeResponse] {

  import io.circe._
  import io.circe.syntax._

  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  implicit val dataEncoder: Encoder[Asset.Data] = extendedEncoder[Asset.Data]
  implicit val imgEncoder: Encoder[Asset.Image] = extendedEncoder[Asset.Image]
  implicit val titleEncoder: Encoder[Asset.Title] = extendedEncoder[Asset.Title]
  implicit val videoEncoder: Encoder[Asset.Video] = extendedEncoder[Asset.Video]

  implicit val oneofDataEncoder: Encoder[Data] = openRtbEncoder[Data]
  implicit val oneofImgEncoder: Encoder[Img] = openRtbEncoder[Img]
  implicit val oneofTitleEncoder: Encoder[Title] = openRtbEncoder[Title]
  implicit val oneofVideoEncoder: Encoder[Video] = openRtbEncoder[Video]

  implicit val assetsOneofEncoder: Encoder[AssetOneof] = protobufOneofEncoder[Asset.AssetOneof] {
    case Asset.AssetOneof.Data(data) => data.asJson
    case Asset.AssetOneof.Img(img) => img.asJson
    case Asset.AssetOneof.Title(title) => title.asJson
    case Asset.AssetOneof.Video(video) => video.asJson
  }

  implicit val linkEncoder: Encoder[Link] = extendedEncoder[Link]

  /**
    * After normal encoding, AssetOneof object key has to be renamed accordingly with the type of it.
    * We get this job done by adding up an extra field with good key naming and delete the old `asset`
    * field afterwards.
    */
  implicit val assetsEncoder: Encoder[Asset] = (asset: Asset) => extendedEncoder[Asset]
    .mapJson(_.mapObject(assetObj =>
      assetObj.add(
        asset.assetOneof match {
          case AssetOneof.Data(_) => "data"
          case AssetOneof.Img(_) => "img"
          case AssetOneof.Title(_) => "title"
          case AssetOneof.Video(_) => "video"
        }, assetObj.apply("asset").get)))
    .mapJson(json => json.hcursor.downField("asset").delete.top.get)
    .apply(asset)

  implicit val eventTrackerEncoder = extendedEncoder[EventTracker]


  private implicit val linkDecoder = er.decoderWithExtensions{
    cursor => for {
      url <- cursor.downField("url").as[String]
      clicktrackers <- cursor.downField("clicktrackers").as[Option[Seq[String]]]
      fallback <- cursor.downField("fallback").as[Option[String]]
    } yield NativeResponse.Link(url = url, clicktrackers = clicktrackers.getOrElse(Seq()), fallback = fallback)
  }


  private implicit val titleDecoder = er.decoderWithExtensions {
    cursor =>
      for {
        text <- cursor.downField("text").as[String]
        len <- cursor.downField("len").as[Option[Int]]
      } yield NativeResponse.Asset.Title(text = text, len = len)
  }

  private implicit val imgDecoder = er.decoderWithExtensions {
    cursor =>
      for {
        imageType <- cursor.downField("type").as[Option[Int]].map(_.map(ImageAssetType.fromValue))
        url <- cursor.downField("url").as[String]
        w <- cursor.downField("w").as[Option[Int]]
        h <- cursor.downField("h").as[Option[Int]]
      } yield NativeResponse.Asset.Image(`type` = imageType, url = url, w = w, h = h)
  }

  private implicit val videoDecoder = er.decoderWithExtensions {
    cursor =>
      for {
        vasttag <- cursor.downField("vasttag").as[String]
      } yield NativeResponse.Asset.Video(vasttag = vasttag)
  }

  private implicit val dataDecoder = er.decoderWithExtensions {
    cursor =>
      for {
        dataType <- cursor.downField("type").as[Option[Int]].map(_.map(DataAssetType.fromValue))
        len <- cursor.downField("len").as[Option[Int]]
        label <- cursor.downField("label").as[Option[String]]
        value <- cursor.downField("value").as[String]
      } yield NativeResponse.Asset.Data(`type` = dataType, len = len, label = label, value = value)
  }
  private implicit val eventTrackerDecoder = er.decoderWithExtensions {
    cursor =>
      for {
        event <- cursor.downField("event").as[Option[Int]].map(_.map(EventType.fromValue))
        method <- cursor.downField("event").as[Int].map(EventTrackingMethod.fromValue)
        url <- cursor.downField("url").as[Option[String]]
      } yield NativeResponse.EventTracker(event = event, method = method, url = url)
  }

  private implicit val assetDecoder = er.decoderWithExtensions {
    cursor =>
      for {
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
  }

  def encoder = unboxedEncoder
    .mapJson(json => Json.obj(("native", json)))

  def unboxedEncoder = extendedEncoder[NativeResponse]
    .clean(toKeep = Seq("assets"))

  def decoder: Decoder[NativeResponse] = er.decoderWithExtensions {
    cursor =>
      for {
        ver <- cursor.downField("ver").as[Option[Json]]
        str = ver.flatMap(_.asString)
        int = ver.flatMap(_.asNumber.flatMap(_.toInt).map(_.toString))
        assets <- cursor.downField("assets").as[Option[Seq[NativeResponse.Asset]]]
        assetsurl <- cursor.downField("assetsurl").as[Option[String]]
        dcourl <- cursor.downField("dcourl").as[Option[String]]
        link <- cursor.downField("link").as[NativeResponse.Link]
        imptrackers <- cursor.downField("imptrackers").as[Option[Seq[String]]]
        jstracker <- cursor.downField("jstracker").as[Option[String]]
        eventtrackers <- cursor.downField("eventtrackers").as[Option[Seq[NativeResponse.EventTracker]]]
        privacy <- cursor.downField("privacy").as[Option[String]]
      } yield {
        NativeResponse(ver = str.orElse(int), assets = assets.getOrElse(Seq()), assetsurl = assetsurl, dcourl = dcourl, link = link,
          imptrackers = imptrackers.getOrElse(Seq()), jstracker = jstracker, eventtrackers = eventtrackers.getOrElse(Seq()), privacy = privacy)
      }
  }

}