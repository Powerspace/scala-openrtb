package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb._
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * OpenRTB SeatBid Serde
  */
object OpenRtbSeatBidSerde {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames


  /**
    * Decoder for the OpenRTB seatBid object.
    */
  implicit def decoder(implicit bidDecoder: Decoder[BidResponse.SeatBid.Bid]): Decoder[BidResponse.SeatBid] =
    cursor => for {
      seat <- cursor.downField("seat").as[Option[String]]
      group <- cursor.downField("group").as[Option[Boolean]]
      bids <- cursor.downField("bid").as[Seq[BidResponse.SeatBid.Bid]]
    } yield {
      BidResponse.SeatBid(seat = seat, bid = bids, group = group)
    }

  private implicit val bidEncoder = OpenRtbBidSerde.encoder

  implicit def encoder: Encoder[BidResponse.SeatBid] =
      deriveEncoder[BidResponse.SeatBid].cleanRtb

}