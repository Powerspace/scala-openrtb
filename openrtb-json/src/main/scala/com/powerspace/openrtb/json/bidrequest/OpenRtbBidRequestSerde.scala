package com.powerspace.openrtb.json.bidrequest

import com.google.openrtb.BidRequest
import com.google.openrtb.BidRequest.{DistributionchannelOneof, Imp, Source}
import com.powerspace.openrtb.json.EncoderProvider
import com.powerspace.openrtb.json.OpenRtbExtensions.ExtensionRegistry
import com.powerspace.openrtb.json.util.EncodingUtils
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumEncoders
import com.powerspace.openrtb.json.common.OpenRtbProtobufEnumDecoders

/**
  * OpenRTB BidRequest Encoder and Decoder
  */
class OpenRtbBidRequestSerde(implicit er: ExtensionRegistry) extends EncoderProvider[BidRequest] {

  private val impressionSerde = new OpenRtbImpressionSerde()

  import io.circe._
  import EncodingUtils._
  import com.google.openrtb._
  import io.circe.syntax._
  import OpenRtbProtobufEnumEncoders._
  import OpenRtbProtobufEnumDecoders._

  implicit val metricEncoder: Encoder[Imp.Metric] = impressionSerde.metricEncoder

  implicit val producerEncoder: Encoder[BidRequest.Producer] = extendedEncoder[BidRequest.Producer]
  implicit val publisherEncoder: Encoder[BidRequest.Publisher] = extendedEncoder[BidRequest.Publisher]
  implicit val contentEncoder: Encoder[BidRequest.Content] = extendedEncoder[BidRequest.Content]

  implicit val sourceEncoder: Encoder[Source] = extendedEncoder[Source]
  implicit val deviceEncoder: Encoder[BidRequest.Device] = extendedEncoder[BidRequest.Device]
  implicit val regsEncoder: Encoder[BidRequest.Regs] = extendedEncoder[BidRequest.Regs]

  implicit val distChannelOneOfEncoder: Encoder[BidRequest.DistributionchannelOneof] = protobufOneofEncoder[BidRequest.DistributionchannelOneof] {
    case BidRequest.DistributionchannelOneof.App(app) => app.asJson
    case BidRequest.DistributionchannelOneof.Site(site) => site.asJson
  }

  implicit val appEncoder: Encoder[BidRequest.App] = extendedEncoder[BidRequest.App]
  implicit val siteEncoder: Encoder[BidRequest.Site] = extendedEncoder[BidRequest.Site]
  implicit val geoEncoder: Encoder[BidRequest.Geo] = extendedEncoder[BidRequest.Geo]

  def encoder(implicit userEncoder: Encoder[BidRequest.User], impEncoder: Encoder[BidRequest.Imp]): Encoder[BidRequest] =
    bidRequest => {
      val bidRequestEncoder = extendedEncoder[BidRequest].transformBooleans.clean(toKeep = Seq("imp"))
      /**
        * After normal encoding, DistributionchannelOneof object key has to be renamed accordingly based on the type.
        * We get this job done by adding an extra node with the right naming and deleting the old node afterwards.
        */
      bidRequest.distributionchannelOneof match {
        // if there is no distribution channel, just return the request encoder
        case DistributionchannelOneof.Empty => bidRequestEncoder
        case _ => bidRequestEncoder
          .mapJson(_.mapObject(request =>
            // add the distribution channel node with the right naming
            request.add(
              bidRequest.distributionchannelOneof match {
                case DistributionchannelOneof.App(_) => "app"
                case DistributionchannelOneof.Site(_) => "site"
              }, request.apply("distributionchannel").get)))
          // now delete the old node
          .mapJson(json => json.hcursor.downField("distributionchannel").delete.top.get)
      }
    }.apply(bidRequest)

  private implicit val metricDecoder: Decoder[Imp.Metric] = impressionSerde.metricDecoder

  implicit val producerDecoder: Decoder[BidRequest.Producer] = extendedDecoder[BidRequest.Producer]
  implicit val publisherDecoder: Decoder[BidRequest.Publisher] = extendedDecoder[BidRequest.Publisher]
  implicit val contentDecoder: Decoder[BidRequest.Content] = extendedDecoder[BidRequest.Content]

  implicit val appDecoder: Decoder[BidRequest.App] = extendedDecoder[BidRequest.App]
  implicit val siteDecoder: Decoder[BidRequest.Site] = extendedDecoder[BidRequest.Site]
  implicit val geoDecoder: Decoder[BidRequest.Geo] = extendedDecoder[BidRequest.Geo]

  implicit val channelSiteDecoder: Decoder[DistributionchannelOneof.Site] = openRtbDecoder[DistributionchannelOneof.Site]
  implicit val channelAppDecoder: Decoder[DistributionchannelOneof.App] = openRtbDecoder[DistributionchannelOneof.App]

  implicit val sourceDecoder: Decoder[BidRequest.Source] = extendedDecoder[BidRequest.Source]
  implicit val deviceDecoder: Decoder[BidRequest.Device] = extendedDecoder[BidRequest.Device]
  implicit val regsDecoder: Decoder[BidRequest.Regs] = extendedDecoder[BidRequest.Regs]

  implicit val oneOf: Decoder[DistributionchannelOneof] = (c: HCursor) => Right(DistributionchannelOneof.Empty)

  def decoder(implicit userDecoder: Decoder[BidRequest.User], impDecoder: Decoder[BidRequest.Imp]): Decoder[BidRequest] = {
    for {
      bidRequest <- extendedDecoder[BidRequest]
      app <- Decoder[Option[BidRequest.App]].prepare(_.downField("app"))
      site <- Decoder[Option[BidRequest.Site]].prepare(_.downField("site"))
      oneof = site
        .map(DistributionchannelOneof.Site)
        .orElse(app.map(DistributionchannelOneof.App))
        .getOrElse(DistributionchannelOneof.Empty)
    } yield bidRequest.copy(distributionchannelOneof = oneof)
  }

}
