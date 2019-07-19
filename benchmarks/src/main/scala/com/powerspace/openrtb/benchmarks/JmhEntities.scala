package com.powerspace.openrtb.benchmarks

import java.util.UUID

import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.{Geo, Imp, User}
import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.BidResponse.SeatBid.Bid
import com.google.openrtb.BidResponse.SeatBid.Bid.AdmOneof
import com.google.openrtb.NativeResponse.Asset.{AssetOneof, Title}
import com.google.openrtb.NativeResponse.{Asset, Link}
import com.google.openrtb._
import com.powerspace.openrtb.benchmarks.serde.BenchmarkSerdeModule
import com.powerspace.openrtb.example.{BidResponseExt, ExampleProto, ImpExt}

object JmhEntities {

  val defaultBidRequest: BidRequest = BidRequest.defaultInstance

  val simpleBidRequest: BidRequest = BidRequest(
    id = UUID.randomUUID().toString,
    wseat = Seq("seat-1", "seat-2", "seat-3"),
    cur = Seq("EUR", "USD"),
    test = None,
    user = Some(
      User(
        id = Some(UUID.randomUUID().toString),
        geo = Some(
          Geo(
            lat = Some(48.8566),
            lon = Some(2.3522),
            country = Some("fr")
          ))
      )),
    imp = Seq(
      Imp(
        id = "1",
        native = Some(
          Native(
            requestOneof = RequestOneof.RequestNative(
              NativeRequest(
                plcmtcnt = Some(5),
                ver = Some("1"),
                context = Some(ContextType.CONTENT),
                plcmttype = Some(PlacementType.IN_FEED)
              )
            )
          ))
      ))
  )

  val bidRequestWithExtensions: BidRequest = simpleBidRequest.copy(
    imp = simpleBidRequest.imp
      .map(
        _.withExtension(ExampleProto.impExt)(
          Some(ImpExt(Some("test-extension")))
        ))
  )

  val defaultBidResponse: BidResponse = BidResponse.defaultInstance

  val simpleBidResponse: BidResponse = BidResponse(
    id = UUID.randomUUID().toString,
    seatbid = Seq(
      SeatBid(
        bid = Seq(
          Bid(
            id = "1",
            impid = "imp-1",
            price = 1,
            admOneof = AdmOneof.AdmNative(
              NativeResponse(
                ver = Some("1.1"),
                assets = Seq(
                  Asset(
                    id = 1,
                    assetOneof = AssetOneof.Title(
                      value = Title(
                        text = "title"
                      )
                    )
                  )
                ),
                link = Link(
                  url = "https://powerspace.com/"
                )
              )
            )
          ),
          Bid(
            id = "2",
            impid = "imp-2",
            price = 1.5
          ),
          Bid(
            id = "3",
            impid = "imp-3",
            price = 1
          )
        )
      )
    ),
    cur = Some("EUR")
  )

  val bidResponseWithExtensions: BidResponse = simpleBidResponse
    .withExtension(ExampleProto.bidResponseExt)(Some(BidResponseExt(Some("test"))))

  val benchmarkSerdeModule = new BenchmarkSerdeModule

}
