package com.powerspace.openrtb.examples.rtb.http4s.adserver

import cats.effect.Resource
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.openrtb.examples.rtb.http4s.common.ExampleSerdeModule
import io.circe.{Encoder, Json}
import monix.eval.Task
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.duration.Duration

object AdserverApp extends App {

  import monix.execution.Scheduler.Implicits.global

  val httpClient: Resource[Task, Client[Task]] = buildHttpClient()
  val potentialBidResponse = httpBid(httpClient)
  private val bidRequest = Adserver.buildBidRequest()

  potentialBidResponse
    .map(bidResponse => {
      bidResponse.foreach(br => println(buildAuctionString(br)))
    })
    .runSyncUnsafe(Duration.Inf)

  private def buildHttpClient(): Resource[Task, Client[Task]] = {
    BlazeClientBuilder[Task](global).resource
  }

  private def httpBid(httpClient: Resource[Task, Client[Task]]) =
    httpClient.use(AdserverHttpClientBuilder.bid(_, bidRequest))

  private def buildAuctionString(bidResponse: BidResponse) = {
    case class Auction(bidRequest: BidRequest, bidResponse: BidResponse)

    val auctionEncoder = new Encoder[Auction] {
      override def apply(auction: Auction): Json = Json.obj(
        ("request", ExampleSerdeModule.bidRequestEncoder.apply(auction.bidRequest)),
        ("response", ExampleSerdeModule.bidResponseEncoder.apply(auction.bidResponse))
      )
    }

    auctionEncoder(Auction(bidRequest, bidResponse)).toString()
  }
}
