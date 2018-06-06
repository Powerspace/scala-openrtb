package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.NativeResponse
import com.powerspace.bidswitch.{BidswitchProto, NativeResponseExt}
import com.powerspace.openrtb.json.bidresponse.NativeSerde
import io.circe.Decoder

object BidSwitchNativeDecoder {

  /**
    * Decoder for the native extension object.
    * `viewtracker` represents the view tracking URL that will be called when the ad is visible, if supported by the Supplier.
    * `adchoiceurl` is a Buyer specific AdChoices URL that will replace default Supplier AdChoices URL.
    */
  implicit val nativeExtDecoder: Decoder[NativeResponseExt] = {
    cursor =>
      val ext = cursor.downField("ext")
      for {
        viewtracker <- ext.downField("viewtracker").as[Option[String]]
        adchoiceurl <- ext.downField("adchoiceurl").as[Option[String]]
      } yield NativeResponseExt(viewtracker = viewtracker, adchoiceurl = adchoiceurl)
  }

  implicit def decoder: Decoder[NativeResponse] = for {
    native <- NativeSerde.decoder
    ext <- nativeExtDecoder
  } yield native.withExtension(BidswitchProto.responseNativeExt)(Some(ext))

}
