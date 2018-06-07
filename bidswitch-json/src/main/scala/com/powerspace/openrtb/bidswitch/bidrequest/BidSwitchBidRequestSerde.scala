package com.powerspace.openrtb.bidswitch.bidrequest

import com.google.openrtb.BidRequest
import com.powerspace.bidswitch.BidRequestExt.Google.DetectedVertical
import com.powerspace.bidswitch.BidRequestExt.{AdTruth, AdsTxt, Dooh, Google, Gumgum, Publisher, Rubicon, Tv}
import com.powerspace.bidswitch.{BidRequestExt, BidswitchProto}
import com.powerspace.openrtb.bidswitch.util.JsonUtils
import com.powerspace.openrtb.json.bidrequest.OpenRtbBidRequestSerde
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.generic.extras.Configuration

/**
  * Bid request BidSwitch extension encoders
  */
object BidSwitchBidRequestSerde {

  import EncodingUtils._
  import JsonUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import io.circe.syntax._

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

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

  implicit def encoder(implicit userEncoder: Encoder[BidRequest.User], impEncoder: Encoder[BidRequest.Imp]):
    Encoder[BidRequest] = bidRequest =>
    OpenRtbBidRequestSerde.encoder.apply(bidRequest).addExtension(bidRequest.extension(BidswitchProto.bidRequestExt).asJson)

}
