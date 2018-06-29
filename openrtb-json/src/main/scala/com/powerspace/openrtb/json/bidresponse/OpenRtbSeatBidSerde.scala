package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb._
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB SeatBid Encoder and Decoder
  * @todo split up decoder and encoder
  */
object OpenRtbSeatBidSerde extends EncoderProvider[BidResponse.SeatBid] {

  import io.circe._
  import io.circe.generic.extras.semiauto._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._

  implicit def encoder(implicit bidEncoder: Encoder[BidResponse.SeatBid.Bid]): Encoder[BidResponse.SeatBid] =
    deriveEncoder[BidResponse.SeatBid].cleanRtb

  implicit def decoder(implicit bidDecoder: Decoder[BidResponse.SeatBid.Bid]): Decoder[BidResponse.SeatBid] =
    cursor => for {
      seat <- cursor.downField("seat").as[Option[String]]
      group <- cursor.downField("group").as[Option[Boolean]]
      bids <- cursor.downField("bid").as[Seq[BidResponse.SeatBid.Bid]]
    } yield {
      BidResponse.SeatBid(seat = seat, bid = bids, group = group)
    }

}