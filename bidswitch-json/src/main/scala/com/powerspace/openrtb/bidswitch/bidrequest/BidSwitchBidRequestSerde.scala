package com.powerspace.openrtb.bidswitch.bidrequest

import com.powerspace.bidswitch.BidRequestExt
import com.powerspace.bidswitch.BidRequestExt.Google.DetectedVertical
import com.powerspace.bidswitch.BidRequestExt.{AdTruth, AdsTxt, Dooh, Google, Gumgum, Publisher, Rubicon, Tv}
import com.powerspace.openrtb.json.ConfiguredSerde
import com.powerspace.openrtb.json.util.EncodingUtils

/**
  * Bid request BidSwitch extension encoders
  */
object BidSwitchBidRequestSerde extends ConfiguredSerde {

  import EncodingUtils._
  import io.circe._

  implicit val detectedVerticalEncoder: Encoder[DetectedVertical] = openRtbEncoder[DetectedVertical]
  implicit val publisherEncoder: Encoder[Publisher] = openRtbEncoder[Publisher]
  implicit val adsTxtEncoder: Encoder[AdsTxt] = openRtbEncoder[AdsTxt]
  implicit val googleEncoder: Encoder[Google] = openRtbEncoder[Google]
  implicit val gumGumEncoder: Encoder[Gumgum] = openRtbEncoder[Gumgum]
  implicit val rubiconEncoder: Encoder[Rubicon] = openRtbEncoder[Rubicon]
  implicit val adTruthEncoder: Encoder[AdTruth] = openRtbEncoder[AdTruth]
  implicit val tvEncoder: Encoder[Tv] = openRtbEncoder[Tv]
  implicit val doohEncoder: Encoder[Dooh] = openRtbEncoder[Dooh]

  val bidRequestExtEncoder: Encoder[BidRequestExt] = openRtbEncoder[BidRequestExt]

  implicit val detectedVerticalDecoder: Decoder[DetectedVertical] = openRtbDecoder[DetectedVertical]
  implicit val publisherDecoder: Decoder[Publisher] = openRtbDecoder[Publisher]
  implicit val adsTxtDecoder: Decoder[AdsTxt] = openRtbDecoder[AdsTxt]
  implicit val googleDecoder: Decoder[Google] = openRtbDecoder[Google]
  implicit val gumGumDecoder: Decoder[Gumgum] = openRtbDecoder[Gumgum]
  implicit val rubiconDecoder: Decoder[Rubicon] = openRtbDecoder[Rubicon]
  implicit val adTruthDecoder: Decoder[AdTruth] = openRtbDecoder[AdTruth]
  implicit val tvDecoder: Decoder[Tv] = openRtbDecoder[Tv]
  implicit val doohDecoder: Decoder[Dooh] = openRtbDecoder[Dooh]

  val bidRequestExtDecoder: Decoder[BidRequestExt] = openRtbDecoder[BidRequestExt]

}
