package com.powerspace.openrtb.bidswitch

import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.bidswitch.bidrequest._
import com.powerspace.openrtb.bidswitch.bidresponse.{BidSwitchBidSerde, BidSwitchBidResponseSerde}
import com.powerspace.openrtb.json._
import io.circe.generic.extras.Configuration

/**
  * BidSwitch Serialization and Deserialization Module
  * It uses OpenRTB Serdes underneath and applies extensions Serdes at BidResponse and Bid levels
  */
object BidSwitchSerdeModule extends SerdeModule {

  import io.circe._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  /**
    * BidSwitch bid response decoders
    */
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = BidSwitchBidSerde.bidDecoder
  override implicit val seatBidDecoder: Decoder[SeatBid] = BidSwitchBidSerde.seatBidDecoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = BidSwitchBidResponseSerde.decoder

  /**
    * BidSwitch bid response encoders
    */
  override implicit val bidEncoder: Encoder[SeatBid.Bid] = BidSwitchBidSerde.bidEncoder
  override implicit val seatBidEncoder: Encoder[SeatBid] = BidSwitchBidSerde.seatBidEncoder
  override implicit val bidResponseEncoder: Encoder[BidResponse] = BidSwitchBidResponseSerde.encoder

  /**
    * BidSwitch bid request encoders
    */
  override implicit val userEncoder: Encoder[BidRequest.User] = BidSwitchUserSerde.encoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = BidSwitchImpressionSerde.encoder
  override implicit val bidRequestEncoder: Encoder[BidRequest] = BidSwitchBidRequestSerde.encoder

}
