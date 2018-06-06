package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb._
import io.circe.Decoder

/**
  * OpenRTB SeatBid Serde
  */
object OpenRtbSeatBidSerde {

  /**
    * Decoder for the OpenRTB seatBid object.
    */
  implicit def decoder(implicit seatBidDecoder: Decoder[BidResponse.SeatBid.Bid]): Decoder[BidResponse.SeatBid] =
    cursor => for {
      seat <- cursor.downField("seat").as[Option[String]]
      group <- cursor.downField("group").as[Option[Boolean]]
      bids <- cursor.downField("bid").as[Seq[BidResponse.SeatBid.Bid]]
    } yield {
      BidResponse.SeatBid(seat = seat, bid = bids, group = group)
    }

}