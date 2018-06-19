package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.{BidResponse, NoBidReason}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * OpenRTB BidResponse Encoder and Decoder
  * @todo split up decoder and encoder
  */
object OpenRtbBidResponseSerde extends EncoderProvider[BidResponse] {

  import io.circe._
  import EncodingUtils._
  import OpenRtbProtobufEnumEncoders._
  import io.circe.generic.extras.semiauto._

  private implicit val seatBidEncoder: Encoder[BidResponse.SeatBid] = OpenRtbSeatBidSerde.encoder
  def encoder: Encoder[BidResponse] = deriveEncoder[BidResponse].cleanRtb

  private implicit val noBidReasonDecoder: Decoder[Option[NoBidReason]] = Decoder.decodeOption[Int].map(_.map(NoBidReason.fromValue))
  def decoder(implicit seatBidDecoder: Decoder[BidResponse.SeatBid]): Decoder[BidResponse] = {
    cursor =>
      for {
        id <- cursor.downField("id").as[String]
        seatBids <- cursor.downField("seatbid").as[Option[Seq[BidResponse.SeatBid]]]
        bidid <- cursor.downField("bidid").as[Option[String]]
        cur <- cursor.downField("cur").as[Option[String]]
        customdata <- cursor.downField("customdata").as[Option[String]]
        nbr <- cursor.downField("nbr").as[Option[Int]].map(_.map(NoBidReason.fromValue))
      } yield {
        BidResponse(id = id, seatbid = seatBids.getOrElse(Seq()), bidid = bidid, cur = cur, customdata = customdata, nbr = nbr)
      }
  }

}
