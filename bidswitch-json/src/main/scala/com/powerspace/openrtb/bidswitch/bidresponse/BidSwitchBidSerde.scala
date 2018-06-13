package com.powerspace.openrtb.bidswitch.bidresponse

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb.NativeResponse
import com.powerspace.bidswitch.BidExt.{Google, Yieldone}
import com.powerspace.bidswitch.{BidExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidresponse.{OpenRtbBidSerde, OpenRtbNativeSerde, OpenRtbSeatBidSerde}
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Decoder for bid and seat bid objects and extension
  */
object BidSwitchBidSerde {

  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.extras.semiauto._
  import JsonUtils._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * Decoding
    */
  implicit val nativeResponseDecoder: Decoder[NativeResponse] = BidSwitchNativeSerde.decoder

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

  private val bidExtDecoder: Decoder[BidExt] =
    cursor => {
      val ext = cursor.downField("ext")
      for {
        asid <- ext.downField("asid").as[Option[String]]
        country <- ext.downField("country").as[Option[String]]
        advertiserName <- ext.downField("advertiser_name").as[Option[String]]
        agencyName <- ext.downField("agency_name").as[Option[String]]
        agencyId <- ext.downField("agency_id").as[Option[String]]
        lpdomain <- ext.downField("lpdomain").as[Option[Seq[String]]]
        language <- ext.downField("language").as[Option[String]]
        vastUrl <- ext.downField("vast_url").as[Option[String]]
        daastUrl <- ext.downField("daast_url").as[Option[String]]
        duration <- ext.downField("duration").as[Option[Int]]
        deal <- ext.downField("deal").as[Option[String]]
        imgUrl <- ext.downField("img_url").as[Option[String]]
        clickUrl <- ext.downField("click_url").as[Option[String]]
        jsUrl <- ext.downField("js_url").as[Option[String]]
        native <- ext.downField("native").as[Option[NativeResponse]]
        google <- ext.downField("google").as[Option[Google]]
        yieldone <- ext.downField("yieldone").as[Option[Yieldone]]
      } yield {
        BidExt(asid = asid, country = country, advertiserName = advertiserName, agencyName = agencyName,
          agencyId = agencyId, lpdomain = lpdomain.getOrElse(Seq()), language = language, vastUrl = vastUrl,
          daastUrl = daastUrl, duration = duration, deal = deal, imgUrl = imgUrl, clickUrl = clickUrl, jsUrl = jsUrl,
          native = native, google = google, yieldone = yieldone)
      }
    }

  implicit def bidDecoder: Decoder[SeatBid.Bid] = for {
    bid <- OpenRtbBidSerde.decoder
    ext <- bidExtDecoder
  } yield bid.withExtension(BidswitchProto.bidExt)(Some(ext))

  implicit def seatBidDecoder: Decoder[SeatBid] = OpenRtbSeatBidSerde.decoder(bidDecoder)

  /**
    * Encoding
    */
  implicit val googleEncoder: Encoder[Google] = deriveEncoder[Google].cleanRtb
  implicit val yieldoneEncoder: Encoder[Yieldone] = deriveEncoder[Yieldone].cleanRtb

  implicit val nativeResponseEncoder: Encoder[NativeResponse] = OpenRtbNativeSerde.nativeResponseEncoder
  implicit val bidExtensionEncoder: Encoder[BidExt] = deriveEncoder[BidExt].cleanRtb
  implicit val admOneofEncoder: Encoder[AdmOneof] = OpenRtbBidSerde.admOneofEncoder

  implicit val bidEncoder: Encoder[SeatBid.Bid] =
    bid => OpenRtbBidSerde.encoder.apply(bid).addExtension(bid.extension(BidswitchProto.bidExt).asJson)

  def seatBidEncoder: Encoder[SeatBid] =
    seatBid => OpenRtbSeatBidSerde.encoder.apply(seatBid)

}
