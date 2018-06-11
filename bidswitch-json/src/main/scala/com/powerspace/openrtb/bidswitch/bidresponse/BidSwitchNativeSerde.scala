package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.NativeResponse
import com.powerspace.bidswitch.{BidswitchProto, NativeResponseExt}
import com.powerspace.openrtb.json.bidresponse.OpenRtbNativeSerde
import io.circe.Decoder

object BidSwitchNativeSerde {

  implicit val nativeExtDecoder: Decoder[NativeResponseExt] = {
    cursor =>
      val ext = cursor.downField("ext")
      for {
        viewtracker <- ext.downField("viewtracker").as[Option[String]]
        adchoiceurl <- ext.downField("adchoiceurl").as[Option[String]]
      } yield NativeResponseExt(viewtracker = viewtracker, adchoiceurl = adchoiceurl)
  }

  implicit def decoder: Decoder[NativeResponse] = for {
    native <- OpenRtbNativeSerde.decoder
    ext <- nativeExtDecoder
  } yield native.withExtension(BidswitchProto.responseNativeExt)(Some(ext))

}
