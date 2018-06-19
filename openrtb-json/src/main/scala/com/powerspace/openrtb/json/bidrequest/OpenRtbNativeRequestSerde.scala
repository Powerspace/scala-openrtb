package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.NativeRequest.Asset.AssetOneof
import com.google.openrtb.{APIFramework, CreativeAttribute, NativeRequest}
import com.google.openrtb.NativeRequest.{Asset, EventTrackers}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB Native Encoder and Decoder
  * @todo split up decoder and encoder
  */
object OpenRtbNativeRequestSerde extends EncoderProvider[Imp.Native] {

  import io.circe._
  import io.circe.syntax._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._

  private implicit val titleEncoder: Encoder[NativeRequest.Asset.Title] = openRtbEncoder[Asset.Title]
  private implicit val imgEncoder: Encoder[NativeRequest.Asset.Image] = openRtbEncoder[Asset.Image]
  private implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.encoder
  private implicit val assetDataEncoder: Encoder[NativeRequest.Asset.Data] = openRtbEncoder[Asset.Data]
  private implicit val assetOneOfEncoder: Encoder[NativeRequest.Asset.AssetOneof] = protobufOneofEncoder[NativeRequest.Asset.AssetOneof] {
    case NativeRequest.Asset.AssetOneof.Img(img) => img.asJson
    case NativeRequest.Asset.AssetOneof.Data(data) => data.asJson
    case NativeRequest.Asset.AssetOneof.Video(video) => video.asJson
    case NativeRequest.Asset.AssetOneof.Title(title) => title.asJson
  }

  private implicit val assetEncoder: Encoder[NativeRequest.Asset] = openRtbEncoder[Asset]
  private implicit val eventTrackersEncoder: Encoder[NativeRequest.EventTrackers] = openRtbEncoder[EventTrackers]
  implicit val nativeRequestEncoder: Encoder[NativeRequest] = openRtbEncoder[NativeRequest]

  private implicit val requestOneOfEncoder: Encoder[Imp.Native.RequestOneof] = protobufOneofEncoder[Imp.Native.RequestOneof] {
    case Imp.Native.RequestOneof.Request(string) => string.asJson
    case Imp.Native.RequestOneof.RequestNative(request) => request.asJson
  }

  def encoder: Encoder[Imp.Native] = openRtbEncoder[Imp.Native]

  private implicit val titleDecoder: Decoder[NativeRequest.Asset.Title] = openRtbDecoder[Asset.Title]
  private implicit val imgDecoder: Decoder[NativeRequest.Asset.Image] = openRtbDecoder[Asset.Image]
  private implicit val videoDecoder: Decoder[Imp.Video] = OpenRtbVideoSerde.decoder
  private implicit val assetDataDecoder: Decoder[NativeRequest.Asset.Data] = openRtbDecoder[Asset.Data]

  /**
    * Each asset object may contain only one of title, img, data or video.
    */
  private implicit val assetDecoder: Decoder[NativeRequest.Asset] = cursor =>
    for {
      id <- cursor.downField("id").as[Int]
      required <- cursor.downField("required").as[Option[Boolean]]
      title <- cursor.downField("title").as[Option[NativeRequest.Asset.Title]].map(_.map(NativeRequest.Asset.AssetOneof.Title))
      img <- cursor.downField("img").as[Option[NativeRequest.Asset.Image]].map(_.map(NativeRequest.Asset.AssetOneof.Img))
      video <- cursor.downField("video").as[Option[Imp.Video]].map(_.map(NativeRequest.Asset.AssetOneof.Video))
      data <- cursor.downField("data").as[Option[NativeRequest.Asset.Data]].map(_.map(NativeRequest.Asset.AssetOneof.Data))
      assetOneof = title
        .orElse(img)
        .orElse(video)
        .orElse(data)
        .getOrElse(AssetOneof.Empty)
    } yield {
      NativeRequest.Asset(id = id, required = required, assetOneof = assetOneof)
    }

  private implicit val eventTrackersDecoder: Decoder[NativeRequest.EventTrackers] = openRtbDecoder[EventTrackers]
  implicit val nativeRequestDecoder: Decoder[NativeRequest] = openRtbDecoder[NativeRequest]

  private implicit val requestOneOfDecoder: Decoder[Imp.Native.RequestOneof] = (cursor: HCursor) =>
    cursor.focus.map {
      case json if json.isString => json.asString.map(RequestOneof.Request).getOrElse(RequestOneof.Empty)
      case json if json.isObject => json.as[NativeRequest].map(RequestOneof.RequestNative).getOrElse(RequestOneof.Empty)
    }.orElse(Some(RequestOneof.Empty)).map(Right(_)).get

  def decoder: Decoder[Imp.Native] = for {
    native <- openRtbDecoder[Imp.Native]
    oneof <- Decoder[Imp.Native.RequestOneof].prepare(_.downField("request"))
  } yield native.copy(requestOneof = oneof)

}
