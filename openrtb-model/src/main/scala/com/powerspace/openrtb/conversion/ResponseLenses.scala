package com.powerspace.openrtb.conversion

import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import monocle.Traversal

object ResponseLenses {

  import SeqTraverse._
  import monocle.function.all._
  import monocle.macros._

  val priceOrdering = Ordering.by[SeatBid.Bid, Double](_.price).reverse

  val seatBidLens = GenLens[BidResponse](_.seatbid)
  val bidLens = GenLens[SeatBid](_.bid)

  /** Traversal from req.imps object */
  val seatBidLensTraversal = seatBidLens.composeTraversal(each)
  val bidLensTraversal = bidLens.composeTraversal(each)

  /** Lens composition for req.imp.native.oneOf */
  val bidTraversal: Traversal[BidResponse, SeatBid.Bid] = seatBidLensTraversal.composeTraversal(bidLensTraversal)

  def getAllBids(bidResponse: BidResponse): List[SeatBid.Bid] = bidTraversal.getAll(bidResponse)

  def takeBestBids(bidResponse: BidResponse)(n: Int) = getAllBids(bidResponse)
    .sorted(priceOrdering)
    .take(n)

}
