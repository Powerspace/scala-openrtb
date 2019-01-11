package com.powerspace.example

import java.util.UUID

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.{as, entity, failWith, path, post}
import akka.stream.ActorMaterializer
import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof.AdmNative
import com.google.openrtb.NativeResponse.Asset.AssetOneof.Title
import com.google.openrtb.NativeResponse.{Asset, Link}
import com.google.openrtb._
import com.powerspace.openrtb.Bidder
import monix.eval.Task
import monix.execution.Scheduler
import monix.reactive.Observable

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

/**
  * @todo maybe create a simple test?
  * @todo make a proper ScalaOpenRTB readme file with example of usage
  * @todo
  */
object BidderApp extends App {

  import akka.http.scaladsl.server.Directives.complete
  import com.powerspace.openrtb.json.OpenRtbSerdeModule._
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  implicit private val system: ActorSystem = ActorSystem()
  implicit private val materializer: ActorMaterializer = ActorMaterializer()
  implicit private val scheduler: Scheduler = Scheduler.global

  import akka.http.scaladsl.server.directives.FutureDirectives._

  val bidder = new RtbBidder()

  val router =
    path("bidOn") {
      post {
        entity(as[BidRequest]) {
          bidRequest =>
            onComplete(
              bidder.bidOn(bidRequest).runToFuture) {
                case Success(bidResponse) => complete(ToResponseMarshallable(bidResponse))
                case Failure(e) => failWith(e)
              }
        }
      }
    }

  Http().bindAndHandle(router, "0.0.0.0", 8080)

  sys.addShutdownHook {
    system.terminate()
  }
}

class RtbBidder extends Bidder[Task] {
  import cats.data.OptionT

  override def bidOn(bidRequest: BidRequest): Task[Option[BidResponse]] = {
    val bids: Task[List[Bid]] = Observable
      .fromIterable(bidRequest.imp)
      .flatMap(bidsForImpression)
      .toListL

    bids.map{
      case Nil => None
      case bids@_ => Some(withProtocol(BidResponse(
        id = bidRequest.id,
        seatbid = Seq(SeatBid(bid = bids)))),
      )}
  }

  private def bidsForImpression(impression: BidRequest.Imp): Observable[Bid] = {
    Observable.fromTask((for {
      nativeRequest: NativeRequest <- OptionT.fromOption[Task](impression.native.flatMap(
        _.requestOneof.requestNative
      ))
      nativeResponse: NativeResponse <- OptionT.apply(buildNativeResponse(nativeRequest))

      bid: Bid <- OptionT.apply[Task, Bid](buildBid(impression, nativeResponse))
    } yield bid).value).flatMap(Observable.fromIterable(_))
  }

  private def buildNativeResponse(request: NativeRequest): Task[Option[NativeResponse]] = {
    val assets = request.assets.map(
      (asset: NativeRequest.Asset) =>
        NativeResponse.Asset(
          id = asset.id,
          assetOneof = Title(value = Asset.Title(text = "Wonderful offer just for you!"))
        )
    )

    Future.successful(Some(NativeResponse(assets = assets, link = Link(url = "http://www.wonderful-offer.com"), imptrackers = Seq())))
  }

  private def buildBid(impression: BidRequest.Imp, response: NativeResponse): Task[Option[Bid]] = {
    Task.now(Some(Bid(
      id = UUID.randomUUID().toString,
      price = new Random().nextDouble,
      nurl = Some(s"http://www.get-back-to-me.com?impression=${impression.id}"),
      impid = impression.id,
      admOneof = AdmNative(response)
    )))
  }

  private def withProtocol(bidResponse: BidResponse): BidResponse =
    bidResponse.withExtension(ExampleProto.bidResponseExt)(Some(BidResponseExt(protocol = Some("some useful protocol"))))
}
