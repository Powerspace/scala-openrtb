package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.powerspace.openrtb.conversion.RequestLenses
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.bidrequest.{OpenRtbBannerSerde, OpenRtbNativeRequestSerde, OpenRtbVideoSerde}
import io.circe.Encoder

/**
  * Manipulation of bid requests
  **/
class RequestManipulation(implicit er: ExtensionRegistry) {

  import RequestLenses._

  val toNativeAsString: BidRequest => BidRequest = bidRequestToRequestOneOf.modify {
    case RequestOneof.RequestNative(native) => RequestOneof.Request(new OpenRtbNativeRequestSerde(new OpenRtbVideoSerde(new OpenRtbBannerSerde())).nativeRequestEncoder(native).noSpaces)
    case str: RequestOneof.Request => str
  }


  /** specific encoder that always return native as string request **/
  def toNativeStringEncoder: Encoder[BidRequest] = OpenRtbSerdeModule.bidRequestEncoder.contramap(toNativeAsString)

}
