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

  implicit val detectedVerticalEncoder: Encoder[DetectedVertical] = deriveEncoder[DetectedVertical].transformBooleans.clean
  implicit val publisherEncoder: Encoder[Publisher] = deriveEncoder[Publisher].transformBooleans.clean
  implicit val adsTxtEncoder: Encoder[AdsTxt] = deriveEncoder[AdsTxt].transformBooleans.clean
  implicit val googleEncoder: Encoder[Google] = deriveEncoder[Google].transformBooleans.clean
  implicit val gumGumEncoder: Encoder[Gumgum] = deriveEncoder[Gumgum].transformBooleans.clean
  implicit val rubiconEncoder: Encoder[Rubicon] = deriveEncoder[Rubicon].transformBooleans.clean
  implicit val adTruthEncoder: Encoder[AdTruth] = deriveEncoder[AdTruth].transformBooleans.clean
  implicit val tvEncoder: Encoder[Tv] = deriveEncoder[Tv].transformBooleans.clean
  implicit val doohEncoder: Encoder[Dooh] = deriveEncoder[Dooh].transformBooleans.clean
  implicit val bidRequestExt: Encoder[BidRequestExt] = deriveEncoder[BidRequestExt].transformBooleans.clean

  implicit def encoder(implicit userEncoder: Encoder[BidRequest.User], impEncoder: Encoder[BidRequest.Imp]):
    Encoder[BidRequest] = bidRequest =>
    OpenRtbBidRequestSerde.encoder.apply(bidRequest).addExtension(bidRequest.extension(BidswitchProto.bidRequestExt).asJson)

}
