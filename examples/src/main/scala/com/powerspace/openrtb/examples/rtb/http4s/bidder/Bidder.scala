package com.powerspace.openrtb.examples.rtb.http4s.bidder

import cats.Applicative
import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof.AdmNative
import com.google.openrtb.NativeResponse.Asset.AssetOneof.Title
import com.google.openrtb.NativeResponse.{Asset, Link}
import com.google.openrtb.{BidRequest, BidResponse, NativeRequest, NativeResponse}
import com.powerspace.openrtb.example.{BidResponseExt, ExampleProto}
import com.powerspace.openrtb.{Bidder => OpenrtbBidder}

import scala.util.Random

class Bidder[F[_]: Applicative] extends OpenrtbBidder[F] {

  import cats.implicits._

  /**
    * Handle the bidding process
    */
  override def bidOn(bidRequest: BidRequest): F[Option[BidResponse]] = {
    val bidResponse: F[Option[BidResponse]] = bidOnImps(bidRequest.imp).map {
      case Nil => None
      case bids @ _ =>
        BidResponse(
          id = bidRequest.id,
          seatbid = Seq(
            SeatBid(
              bid = bids.toSeq,
              seat = Some("seat-1")
            )),
          bidid = Some("1")
        ).some
    }

    bidResponse
      .map(
        _.map(withProtocol)
      )
  }

  /**
    * Add the protocol extension on the bidResponse define in example.proto
    */
  private def withProtocol(bidResponse: BidResponse) =
    bidResponse.withExtension(ExampleProto.bidResponseExt)(
      Some(BidResponseExt(Some("define your protocol")))
    )

  /**
    * Provide a list of [[com.google.openrtb.BidResponse.SeatBid.Bid]] given a list
    * of [[com.google.openrtb.BidRequest.Imp]]
    */
  private def bidOnImps(imps: Traversable[Imp]): F[Traversable[Bid]] = {
    imps
      .flatMap(bidOnImp)
      .pure[F]
  }

  private def bidOnImp(imp: Imp): Option[Bid] = {
    for {
      nativeRequest <- imp.native.flatMap(_.requestOneof.requestNative)

      nativeResponse <- buildNativeResponse(nativeRequest)

      bid <- buildBid(imp.id, nativeResponse)
    } yield bid
  }

  private def buildNativeResponse(request: NativeRequest): Option[NativeResponse] = {
    val assets: Seq[Asset] = request.assets
      .map(
        (asset: NativeRequest.Asset) =>
          NativeResponse.Asset(
            id = asset.id,
            assetOneof = Title(value = Asset.Title(text = "Wonderful offer just for you!"))
        )
      )

    NativeResponse(
      assets = assets,
      link = Link(url = "http://click-on-me.com"),
      imptrackers = Seq()
    ).some
  }

  private def buildBid(impId: String, response: NativeResponse): Option[Bid] = {
    Bid(
      id = Random.nextInt().toString,
      price = Random.nextDouble(),
      nurl = Some(s"http://www.get-back-to-me.com?impression=$impId"),
      impid = impId,
      admOneof = AdmNative(response),
      cat = Seq(
        "IAB18-3"
      )
    ).some
  }
}
