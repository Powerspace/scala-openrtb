package com.powerspace.openrtb.examples.rtb.http4s.adserver

import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.openrtb.examples.rtb.http4s.common.ExampleSerdeModule
import com.powerspace.openrtb.json.SerdeModule
import io.circe.{Decoder, Encoder}
import monix.eval.Task
import org.http4s.Uri.{Authority, RegName, Scheme}
import org.http4s.client.Client
import org.http4s.{EntityDecoder, EntityEncoder, Method, Request, Uri}

object AdserverHttpClientBuilder {

  import org.http4s.circe._

  val serdeModule: SerdeModule = ExampleSerdeModule

  implicit val bidRequestEncoder: Encoder[BidRequest] = serdeModule.bidRequestEncoder
  implicit val bidRequestEntityEncoder: EntityEncoder[Task, BidRequest] = jsonEncoderOf[Task, BidRequest]

  implicit val bidResponseDecoder: Decoder[BidResponse] = serdeModule.bidResponseDecoder
  implicit val bidResponseEntityDecoder: EntityDecoder[Task, BidResponse] = jsonOf[Task, BidResponse]

  def bid(client: Client[Task], bidRequest: BidRequest): Task[Option[BidResponse]] = {
    val url = Uri(
      scheme = Some(Scheme.http),
      authority = Some(Authority(host = RegName("localhost"), port = Some(9000))),
      path = "/bid"
    )

    val httpRequest = Request[Task](
      method = Method.POST,
      uri = url
    ).withEntity[BidRequest](bidRequest)

    client.expectOption[BidResponse](httpRequest)
  }
}
