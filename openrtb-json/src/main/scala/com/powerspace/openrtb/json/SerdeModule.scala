package com.powerspace.openrtb.json

import com.google.openrtb.{BidRequest, BidResponse}
import io.circe.{Decoder, Encoder}

trait SerdeModule {

  /**
    * Bid request encoders
    */
  implicit val userEncoder: Encoder[BidRequest.User]
  implicit val impEncoder: Encoder[BidRequest.Imp]
  implicit val bidRequestEncoder: Encoder[BidRequest]

  /**
    * Bid request decoders
    */
  implicit val userDecoder: Decoder[BidRequest.User]
  implicit val impDecoder: Decoder[BidRequest.Imp]
  implicit val bidRequestDecoder: Decoder[BidRequest]

  /**
    * Bid response encoders
    */
  implicit val bidEncoder: Encoder[BidResponse.SeatBid.Bid]
  implicit val seatBidEncoder: Encoder[BidResponse.SeatBid]
  implicit val bidResponseEncoder: Encoder[BidResponse]

  /**
    * Bid response decoders
    */
  implicit val bidDecoder: Decoder[BidResponse.SeatBid.Bid]
  implicit val seatBidDecoder: Decoder[BidResponse.SeatBid]
  implicit val bidResponseDecoder: Decoder[BidResponse]

}

