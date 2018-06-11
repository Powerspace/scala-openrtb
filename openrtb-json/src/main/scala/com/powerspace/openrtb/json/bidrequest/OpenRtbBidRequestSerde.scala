package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest.{Imp, Source}
import com.google.openrtb._
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.util.EncodingUtils
import io.circe.Decoder.Result
import io.circe.Encoder
import io.circe.generic.extras.Configuration


/**
  * OpenRTB BidRequest Serde
  */
object OpenRtbBidRequestSerde {

  import EncodingUtils._
  import io.circe._
  import io.circe.generic.extras.semiauto._
  import OpenRtbProtobufEnumEncoders._

  private implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  import io.circe.syntax._

  implicit val metricEncoder: Encoder[Imp.Metric] = OpenRtbImpressionSerde.metricEncoder

  implicit val producerEncoder: Encoder[BidRequest.Producer] = openrtbEncoder[BidRequest.Producer]
  implicit val publisherEncoder: Encoder[BidRequest.Publisher] = openrtbEncoder[BidRequest.Publisher]
  implicit val contentEncoder: Encoder[BidRequest.Content] = openrtbEncoder[BidRequest.Content]


  implicit val sourceEncoder: Encoder[Source] = openrtbEncoder[Source]
  implicit val deviceEncoder: Encoder[BidRequest.Device] = openrtbEncoder[BidRequest.Device]
  implicit val regsEncoder: Encoder[BidRequest.Regs] = openrtbEncoder[BidRequest.Regs]

  implicit val distChannelOneOfEncoder: Encoder[BidRequest.DistributionchannelOneof] = protobufOneofEncoder[BidRequest.DistributionchannelOneof] {
    case BidRequest.DistributionchannelOneof.App(app) => app.asJson
    case BidRequest.DistributionchannelOneof.Site(site) => site.asJson
  }

  implicit val appEncoder: Encoder[BidRequest.App] = openrtbEncoder[BidRequest.App]
  implicit val siteEncoder: Encoder[BidRequest.Site] = openrtbEncoder[BidRequest.Site]
  implicit val geoEncoder: Encoder[BidRequest.Geo] = openrtbEncoder[BidRequest.Geo]


  def encoder(implicit userEncoder: Encoder[BidRequest.User], impEncoder: Encoder[BidRequest.Imp]): Encoder[BidRequest] = {
    deriveEncoder[BidRequest].transformBooleans.clean(toKeep = Seq("imp"))
  }

  /**
    * Decoder for the OpenRTB bid request.
    */
  def decoder: Decoder[BidRequest] = new Decoder[BidRequest] {
    override def apply(c: HCursor): Result[BidRequest] = ???
  }

}
