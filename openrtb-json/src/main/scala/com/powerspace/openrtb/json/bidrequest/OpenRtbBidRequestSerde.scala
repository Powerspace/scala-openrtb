package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.{Imp, Source}
import com.google.openrtb._
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.Decoder.Result
import io.circe.Encoder
import io.circe.generic.extras.Configuration

trait BidRequestDependencies {
  implicit val userEncoder: Encoder[BidRequest.User]
  implicit val impEncoder: Encoder[BidRequest.Imp]
  implicit val geoEncoder: Encoder[BidRequest.Geo]
}

/**
  * OpenRTB BidRequest Serde
  */
object OpenRtbBidRequestSerde extends BidRequestDependencies {
  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import OpenRtbProtobufEnumEncoders._
  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  import io.circe.syntax._

  private implicit val metricEncoder: Encoder[Imp.Metric] = OpenRtbImpressionSerde.metricEncoder

  private implicit val producerEncoder: Encoder[BidRequest.Producer] = deriveEncoder[BidRequest.Producer].transformBooleans.clean
  private implicit val publisherEncoder: Encoder[BidRequest.Publisher] = deriveEncoder[BidRequest.Publisher].transformBooleans.clean

  private implicit val contentEncoder: Encoder[BidRequest.Content] = deriveEncoder[BidRequest.Content].transformBooleans.clean
  private implicit val appEncoder: Encoder[BidRequest.App] = deriveEncoder[BidRequest.App].transformBooleans.clean

  private implicit val siteEncoder: Encoder[BidRequest.Site] = deriveEncoder[BidRequest.Site].transformBooleans.clean


  private implicit val sourceEncoder: Encoder[Source] = deriveEncoder[Source].transformBooleans.clean()
  private implicit val deviceEncoder: Encoder[BidRequest.Device] = deriveEncoder[BidRequest.Device].transformBooleans.clean
  private implicit val regsEncoder: Encoder[BidRequest.Regs] = deriveEncoder[BidRequest.Regs].transformBooleans.clean
  private implicit val distChannelOneOfEncoder: Encoder[BidRequest.DistributionchannelOneof] = protobufOneofEncoder[BidRequest.DistributionchannelOneof] {
      case BidRequest.DistributionchannelOneof.App(app) => app.asJson
      case BidRequest.DistributionchannelOneof.Site(site) => site.asJson
    }

  implicit val geoEncoder: Encoder[BidRequest.Geo] = deriveEncoder[BidRequest.Geo].transformBooleans.clean
  implicit val userEncoder = OpenRtbUserSerde.encoder
  implicit val impEncoder = OpenRtbImpressionSerde.encoder

  def encoder: Encoder[BidRequest] = {
    deriveEncoder[BidRequest].transformBooleans.clean(toKeep = Seq("imp"))
  }

  /**
    * Decoder for the OpenRTB bid request.
    */
  def decoder: Decoder[BidRequest] = new Decoder[BidRequest] {
    override def apply(c: HCursor): Result[BidRequest] = ???
  }

}
