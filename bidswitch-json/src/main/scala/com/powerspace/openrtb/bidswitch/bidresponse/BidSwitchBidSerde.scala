package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.NativeResponse
import com.powerspace.bidswitch.BidExt
import com.powerspace.bidswitch.BidExt.{Google, Yieldone}
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.bidresponse.OpenRtbNativeSerde
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Decoder for bid and seat bid objects and extension
  */
class BidSwitchBidSerde(nativeSerde: OpenRtbNativeSerde) extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._

  implicit val nativeResponseDecoder: Decoder[NativeResponse] = nativeSerde.decoder
  implicit val nativeResponseEncoder: Encoder[NativeResponse] = nativeSerde.unboxedEncoder

  private implicit val googleDecoder: Decoder[Google] = {
    cursor =>
      for {
        attribute <- cursor.downField("attribute").as[Option[Seq[Int]]]
        vendorType <- cursor.downField("vendor_type").as[Option[Seq[Int]]]
      } yield Google(attribute.getOrElse(Seq()), vendorType.getOrElse(Seq()))
  }

  private implicit val yieldoneDecoder: Decoder[Yieldone] = {
    cursor =>
      for {
        creativeType <- cursor.downField("creative_type").as[Option[String]]
        creativeCategoryId <- cursor.downField("creative_category_id").as[Option[Int]]
      } yield Yieldone(creativeType = creativeType, creativeCategoryId = creativeCategoryId)
  }

  val bidExtDecoder = openRtbDecoder[BidExt]

  /**
    * Encoding
    */
  implicit val googleEncoder: Encoder[Google] = openRtbEncoder[Google]
  implicit val yieldoneEncoder: Encoder[Yieldone] = openRtbEncoder[Yieldone]

  implicit val bidExtEncoder: Encoder[BidExt] = openRtbEncoder[BidExt]
}
