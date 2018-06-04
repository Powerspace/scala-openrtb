package com.powerspace.openrtb.bidswitch

import com.google.openrtb.{BidRequest, BidResponse, NativeResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.bidswitch.BidExt.{Google, Yieldone}
import com.powerspace.bidswitch.{BidExt, BidResponseExt, BidswitchProto, NativeResponseExt}
import com.powerspace.openrtb.json._
import io.circe._

/**
  * BidSwitch Serialization and Deserialization Module
  * It uses OpenRTB Serdes underneath and applies extensions Serdes at BidResponse and Bid levels
  */
object BidSwitchSerdeModule extends SerdeModule {

  /**
    * Decoder for bid response extension object.
    */
  private implicit val bidResponseExtDecoder: Decoder[BidResponseExt] = _.downField("ext")
    .downField("protocol")
    .as[Option[String]]
    .map(BidResponseExt.apply)

  /**
    * Decoder for the native extension object.
    * `viewtracker` represents the view tracking URL that will be called when the ad is visible, if supported by the Supplier.
    * `adchoiceurl` is a Buyer specific AdChoices URL that will replace default Supplier AdChoices URL.
    */
  private implicit val nativeExtDecoder: Decoder[NativeResponseExt] = {
    cursor =>
      val ext = cursor.downField("ext")
      for {
        viewtracker <- ext.downField("viewtracker").as[Option[String]]
        adchoiceurl <- ext.downField("adchoiceurl").as[Option[String]]
      } yield NativeResponseExt(viewtracker = viewtracker, adchoiceurl = adchoiceurl)
  }

  /**
    * Decoder for native object.
    */
  private implicit val nativeResponseDecoder: Decoder[NativeResponse] = for {
    native <- NativeSerde.decoder
    ext <- nativeExtDecoder
  } yield native.withExtension(BidswitchProto.nativeExt)(Some(ext))

  /**
    * Decoder for google object.
    */
  private implicit val googleDecoder: Decoder[Google] = {
    cursor =>
      for {
        attribute <- cursor.downField("attribute").as[Option[Seq[Int]]]
        vendorType <- cursor.downField("vendor_type").as[Option[Seq[Int]]]
      } yield Google(attribute.getOrElse(Seq()), vendorType.getOrElse(Seq()))
  }

  /**
    * Decoder for yieldone object.
    */
  private implicit val yieldoneDecoder: Decoder[Yieldone] = {
    cursor =>
      for {
        creativeType <- cursor.downField("creative_type").as[Option[String]]
        creativeCategoryId <- cursor.downField("creative_category_id").as[Option[Int]]
      } yield Yieldone(creativeType = creativeType, creativeCategoryId = creativeCategoryId)
  }

  /**
    * Decoder for the bid extension object.
    * `asid` specifies the 3rd party ad server in use.
    * `lpdomain` is the actual landing page URL of the creative.
    * `vastUrl` and `daastURL` are URLs pointing to the location of VAST and DAAST documents for bid responses.
    */
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

  /**
    * Decoder for bid object.
    */
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = for {
    bid <- BidSerde.decoder
    ext <- bidExtDecoder
  } yield bid.withExtension(BidswitchProto.bidExt)(Some(ext))

  /**
    * Decoder for seatBid object.
    */
  override implicit val seatBidDecoder: Decoder[SeatBid] = SeatBidSerDe.decoder

  /**
    * Decoder for the BidSwitch bid response.
    */
  override implicit val bidResponseDecoder: Decoder[BidResponse] = for {
    bidResponse <- BidResponseSerde.decoder
    extension <- bidResponseExtDecoder
  } yield bidResponse.withExtension(BidswitchProto.bidResponse)(Some(extension))

  override implicit val bidRequestEncoder: Encoder[BidRequest] = Encoder.instance(_ => Json.True)
}
