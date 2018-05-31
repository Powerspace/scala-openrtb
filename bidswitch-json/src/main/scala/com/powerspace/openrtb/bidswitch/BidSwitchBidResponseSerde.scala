package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.bidswitch.{BidExt, BidResponseExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.BidSwitchBidResponseSerde.bidExtDecoder
import com.powerspace.openrtb.json.{BidResponseSerde, BidSerde}
import io.circe.{Decoder, Encoder}

object BidSwitchModule extends BidResponseSerde {
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = for {
    bid <- BidSerde.bidDecoder
    ext <- bidExtDecoder
  } yield bid.withExtension(BidswitchProto.bidExt)(Some(ext))
}

object BidSwitchBidResponseSerde {
  implicit val bidExtDecoder: Decoder[BidExt] =
    cursor => Right(BidExt())

  implicit val bidResponseExtDecoder: Decoder[BidResponseExt] =
    cursor => cursor.downField("ext")
      .downField("protocol")
      .as[Option[String]]
      .map(protocol => BidResponseExt(protocol = protocol))

  implicit val bidResponseDecoder: Decoder[BidResponse] =
    for {
      bidResponse <- BidSwitchModule.bidResponseDecoder
      extension <- bidResponseExtDecoder
    } yield bidResponse.withExtension(BidswitchProto.bidResponse)(Some(extension))
}
