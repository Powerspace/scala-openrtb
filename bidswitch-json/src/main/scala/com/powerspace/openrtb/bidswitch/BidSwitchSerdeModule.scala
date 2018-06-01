package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.bidswitch.{BidExt, BidResponseExt, BidswitchProto}
import com.powerspace.openrtb.json.{SerdeModule, BidResponseSerde, BidSerde, SeatBidSerDe}
import io.circe.Decoder

/**
  * BidSwitch Serialization and Deserialization Module
  * It uses OpenRTB Serdes underneath and applies extensions Serdes at BidResponse and Bid levels
  */
object BidSwitchSerdeModule extends SerdeModule {

  private val bidResponseExtDecoder: Decoder[BidResponseExt] =
    cursor => cursor.downField("ext")
      .downField("protocol")
      .as[Option[String]]
      .map(BidResponseExt(_))

  private val bidExtDecoder: Decoder[BidExt] =
    cursor => Right(BidExt())

  override implicit val bidResponseDecoder: Decoder[BidResponse] = for {
    bidResponse <- BidResponseSerde.decoder
    extension <- bidResponseExtDecoder
  } yield bidResponse.withExtension(BidswitchProto.bidResponse)(Some(extension))

  override implicit val bidDecoder: Decoder[SeatBid.Bid] = for {
    bid <- BidSerde.decoder
    ext <- bidExtDecoder
  } yield bid.withExtension(BidswitchProto.bidExt)(Some(ext))

  override implicit val seatBidDecoder: Decoder[SeatBid] = SeatBidSerDe.decoder

}
