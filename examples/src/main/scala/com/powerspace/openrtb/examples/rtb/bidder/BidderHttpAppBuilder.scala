package com.powerspace.openrtb.examples.rtb.bidder

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.examples.rtb.common.ExampleSerdeModule
import io.circe.Decoder
import monix.eval.Task
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpApp}

object BidderHttpAppBuilder {

  private val bidder = new Bidder[Task]
  private val dsl = Http4sDsl[Task]
  private val serdeModule = ExampleSerdeModule

  /**
    * This method provide the HttpApp we will expose with our http server
    * This implementation rely on [[monix.eval.Task]] but you can use any effect you want
    */
  def build(): HttpApp[Task] = {
    import dsl._
    import org.http4s.HttpRoutes
    import org.http4s.circe._
    import org.http4s.implicits._

    /**
      * bidRequest decoder required to decode a [[com.google.openrtb.BidRequest]] from a json
      */
    implicit val bidRequestDecoder: Decoder[BidRequest] = serdeModule.bidRequestDecoder
    implicit val bidRequestEntityDecoder: EntityDecoder[Task, BidRequest] = jsonOf[Task, BidRequest]

    HttpRoutes.of[Task] {
      case req@POST -> Root / "bid" =>
        for {
          bidRequest <- req.as[BidRequest]
          response <- handleBid(bidRequest)
        } yield response
    }.orNotFound
  }

  /**
    * Transform a [[com.google.openrtb.BidRequest]] to a [[com.google.openrtb.BidResponse]]
    */
  private def handleBid(bidRequest: BidRequest) = {
    import dsl._
    import org.http4s.circe._

    bidder
      .bidOn(bidRequest)
      .flatMap {
        case Some(bidResponse) =>
          // encode the bidResponse to a json object as part of the http response body
          Ok(serdeModule.bidResponseEncoder(bidResponse))
        case None =>
          Ok()
      }
  }
}
