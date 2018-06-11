package com.powerspace.openrtb.json

import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.powerspace.openrtb.conversion.RequestLenses
import com.powerspace.openrtb.json.bidrequest.OpenRtbNativeRequestSerde

/** Manipulation of native property **/
object NativeManipulation {
  import RequestLenses._

  implicit val impEncoder = OpenRtbSerdeModule.impEncoder



  val toNativeAsString = nativeRequestOneOfTraversal.modify{
    case RequestOneof.RequestNative(native) => RequestOneof.Request(OpenRtbNativeRequestSerde.nativeRequestEncoder(native).noSpaces)
    case str: RequestOneof.Request => str
  }

  /**val toNativeAsObject = nativeRequestOneOfTraversal.modify{
    case native: RequestOneof.RequestNative => native
    case str: RequestOneof.Request => RequestOneof.RequestNative(???) // @todo missing decoder
  }**/

  /** specific encoder that always return native as string request **/
  def toNativeStringEncoder = OpenRtbSerdeModule.bidRequestEncoder.contramap(toNativeAsString)

  /** specific encoder that always return native as object request **/
  //val toNativeObjectEncoder = OpenRtbBidRequestSerde.encoder.contramap(toNativeAsObject)

}
