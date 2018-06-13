package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.NativeRequest
import com.google.openrtb.NativeRequest.{Asset, EventTrackers}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.Encoder
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders

trait NativeDependencies {
  implicit val nativeRequestEncoder: Encoder[NativeRequest]
}

object OpenRtbNativeRequestSerde extends EncoderProvider[Native] with NativeDependencies {

  import io.circe._
  import io.circe.syntax._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  private implicit val titleEncoder: Encoder[NativeRequest.Asset.Title] = openrtbEncoder[Asset.Title]
  private implicit val imgEncoder: Encoder[NativeRequest.Asset.Image] = openrtbEncoder[Asset.Image]

  private implicit val videoEncoder: Encoder[Imp.Video] = OpenRtbVideoSerde.encoder

  private implicit val assetDataEncoder: Encoder[NativeRequest.Asset.Data] = openrtbEncoder[Asset.Data].cleanRtb
  private implicit val assetOneOfEncoder: Encoder[NativeRequest.Asset.AssetOneof] = protobufOneofEncoder[NativeRequest.Asset.AssetOneof] {
    case NativeRequest.Asset.AssetOneof.Img(img) => img.asJson
    case NativeRequest.Asset.AssetOneof.Data(data) => data.asJson
    case NativeRequest.Asset.AssetOneof.Video(video) => video.asJson
    case NativeRequest.Asset.AssetOneof.Title(title) => title.asJson
  }

  private implicit val assetEncoder: Encoder[NativeRequest.Asset] = openrtbEncoder[Asset]
  private implicit val eventTrackersEncoder: Encoder[NativeRequest.EventTrackers] = openrtbEncoder[EventTrackers]

  private implicit val requestOneOfEncoder: Encoder[Imp.Native.RequestOneof] = protobufOneofEncoder[Imp.Native.RequestOneof] {
    case Imp.Native.RequestOneof.Request(string) => string.asJson
    case Imp.Native.RequestOneof.RequestNative(request) => request.asJson
  }

  implicit val nativeRequestEncoder: Encoder[NativeRequest] = openrtbEncoder[NativeRequest]

  def encoder: Encoder[Imp.Native] = openrtbEncoder[Imp.Native]

}
