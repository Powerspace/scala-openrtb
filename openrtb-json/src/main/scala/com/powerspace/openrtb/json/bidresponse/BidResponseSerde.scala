package com.powerspace.openrtb.json.bidresponse

import com.google.openrtb.{BidResponse, NoBidReason}
import io.circe.generic.extras.Configuration
import io.circe.{Decoder, Encoder, Json}
import scalapb.UnknownFieldSet

/**
  * Serialize and Deserialize an OpenRTB BidResponse
  */
object BidResponseSerde {

  /**
    * @todo use semi automatic derivation for decoding
    */
  implicit val configuration: Configuration = Configuration.default.withDefaults
  implicit val noBidReasonDecoder: Decoder[Option[NoBidReason]] = Decoder.decodeOption[Int].map(_.map(NoBidReason.fromValue))
  implicit val unknownFieldSet: Decoder[UnknownFieldSet] = _ => Right(UnknownFieldSet(Map()))

  /**
    * Decoder for the OpenRTB bid response.
    */
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

  /**
    * Encoder for the OpenRTB bid response.
    */
  def encoder(): Encoder[BidResponse] = Encoder.instance(_ => Json.True)

}
