package com.powerspace.openrtb.json

import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb.json.bidrequest.OpenRtbBidRequestSerde.{OpenRtbBidRequestDecoder, OpenRtbBidRequestEncoder}
import com.powerspace.openrtb.json.bidrequest._
import com.powerspace.openrtb.json.bidresponse.{OpenRtbBidResponseSerde, OpenRtbBidSerde, OpenRtbSeatBidSerde}
import io.circe.{Decoder, Encoder}

/**
  * Provides serialization and deserialization for OpenRTB entities.
  */
object OpenRtbSerdeModule extends SerdeModule {

  /**
    * Bid request encoders
    */
  override implicit val userEncoder: Encoder[BidRequest.User] = OpenRtbUserSerde.encoder
  override implicit val impEncoder: Encoder[BidRequest.Imp] = OpenRtbImpressionSerde.encoder(
    ImpressionLevelSerdes.bannerEncoder,
    ImpressionLevelSerdes.videoEncoder,
    ImpressionLevelSerdes.audioEncoder,
    ImpressionLevelSerdes.pmpEncoder,
    OpenRtbNativeRequestSerde.encoder
  )
  override implicit val bidRequestEncoder: Encoder[BidRequest] = OpenRtbBidRequestEncoder.encoder

  /**
    * Bid request decoders
    */
  override implicit val userDecoder: Decoder[BidRequest.User] = OpenRtbUserSerde.decoder
  override implicit val impDecoder: Decoder[BidRequest.Imp] = OpenRtbImpressionSerde.decoder(
    ImpressionLevelSerdes.bannerDecoder,
    ImpressionLevelSerdes.videoDecoder,
    ImpressionLevelSerdes.audioDecoder,
    ImpressionLevelSerdes.pmpDecoder,
    OpenRtbNativeRequestSerde.decoder
  )
  override implicit val bidRequestDecoder: Decoder[BidRequest] = OpenRtbBidRequestDecoder.decoder

  /**
    * Bid response encoders
    */
  override implicit val bidEncoder: Encoder[SeatBid.Bid] = OpenRtbBidSerde.encoder
  override implicit val seatBidEncoder: Encoder[SeatBid] = OpenRtbSeatBidSerde.encoder
  override implicit val bidResponseEncoder: Encoder[BidResponse] = OpenRtbBidResponseSerde.encoder

  /**
    * Bid response decoders
    */
  override implicit val bidDecoder: Decoder[SeatBid.Bid] = OpenRtbBidSerde.decoder
  override implicit val seatBidDecoder: Decoder[BidResponse.SeatBid] = OpenRtbSeatBidSerde.decoder
  override implicit val bidResponseDecoder: Decoder[BidResponse] = OpenRtbBidResponseSerde.decoder

}
