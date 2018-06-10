package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.powerspace.openrtb.conversion.RequestLenses
import com.powerspace.openrtb.json.bidrequest.{OpenRtbBidRequestSerde, OpenRtbImpressionSerde}

/** Manipulation of native property **/
object NativeManipulation {
  import RequestLenses._
  import com.powerspace.openrtb.json.bidrequest.OpenRtbUserSerde._
  import com.powerspace.openrtb.json.bidrequest.OpenRtbPmpSerde._
  import com.powerspace.openrtb.json.bidrequest.OpenRtbImpressionSerde._

  implicit val impEncoder = com.powerspace.openrtb.json.bidrequest.OpenRtbImpressionSerde.encoder

  val toNativeAsString = nativeRequestOneOfTraversal.modify{
    case RequestOneof.RequestNative(native) => RequestOneof.Request(OpenRtbImpressionSerde.nativeRequestEncoder(native).noSpaces)
    case str: RequestOneof.Request => str
  }

  val toNativeAsObject = nativeRequestOneOfTraversal.modify{
    case native: RequestOneof.RequestNative => native
    case str: RequestOneof.Request => RequestOneof.RequestNative(???) // @todo missing decoder
  }

  /** specific encoder that always return native as string request **/
  val toNativeStringEncoder = OpenRtbBidRequestSerde.encoder.contramap(toNativeAsString)

  /** specific encoder that always return native as object request **/
  val toNativeObjectEncoder = OpenRtbBidRequestSerde.encoder.contramap(toNativeAsObject)

}
