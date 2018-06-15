package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.{Imp, User}
import com.powerspace.bidswitch.BidRequestExt.Google.DetectedVertical
import com.powerspace.bidswitch.BidRequestExt.{AdTruth, AdsTxt, Dooh, Google, Gumgum, Publisher, Rubicon, Tv}
import com.powerspace.bidswitch.{BidRequestExt, BidswitchProto}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.bidrequest.OpenRtbBidRequestSerde.OpenRtbBidRequestEncoder
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Bid request BidSwitch extension encoders
  */
object BidSwitchBidRequestSerde extends EncoderProvider[BidRequest]{

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val detectedVerticalEncoder: Encoder[DetectedVertical] = deriveEncoder[DetectedVertical].cleanRtb
  implicit val publisherEncoder: Encoder[Publisher] = deriveEncoder[Publisher].cleanRtb
  implicit val adsTxtEncoder: Encoder[AdsTxt] = deriveEncoder[AdsTxt].cleanRtb
  implicit val googleEncoder: Encoder[Google] = deriveEncoder[Google].cleanRtb
  implicit val gumGumEncoder: Encoder[Gumgum] = deriveEncoder[Gumgum].cleanRtb
  implicit val rubiconEncoder: Encoder[Rubicon] = deriveEncoder[Rubicon].cleanRtb
  implicit val adTruthEncoder: Encoder[AdTruth] = deriveEncoder[AdTruth].cleanRtb
  implicit val tvEncoder: Encoder[Tv] = deriveEncoder[Tv].cleanRtb
  implicit val doohEncoder: Encoder[Dooh] = deriveEncoder[Dooh].cleanRtb
  implicit val bidRequestExt: Encoder[BidRequestExt] = deriveEncoder[BidRequestExt].cleanRtb
  implicit val userEncoder: Encoder[User] = BidSwitchUserSerde.encoder
  implicit val impEncoder: Encoder[Imp] = BidSwitchImpressionSerde.encoder

  def encoder:
    Encoder[BidRequest] = bidRequest => {
    val json: Json = bidRequest.extension(BidswitchProto.bidRequestExt).asJson

    OpenRtbBidRequestEncoder.encoder.apply(bidRequest).asObject
      .map(_.add("ext", json)).asJson
  }
}
