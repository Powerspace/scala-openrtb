package com.powerspace.openrtb.json

import com.google.openrtb.{BidResponse, NoBidReason}
import io.circe.{Decoder, Encoder}

/**
  * Serialize and Deserialize an OpenRTB BidResponse
  */
object BidResponseSerde {

  def decoder(implicit seatBidDecoder: Decoder[BidResponse.SeatBid]): Decoder[BidResponse] = {
    cursor =>
      for {
        id <- cursor.downField("id").as[String]
        bidid <- cursor.downField("bidid").as[Option[String]]
        cur <- cursor.downField("cur").as[Option[String]]
        customdata <- cursor.downField("customdata").as[Option[String]]
        nbr <- cursor.downField("nbr").as[Option[Int]].map(_.map(NoBidReason.fromValue))
        seatBids <- cursor.downField("seatbid").as[Seq[BidResponse.SeatBid]]
      } yield {
        BidResponse(id = id, seatbid = seatBids, bidid = bidid, cur = cur, customdata = customdata, nbr = nbr)
      }
  }

  def encoder(): Encoder[BidResponse] = ???

}
