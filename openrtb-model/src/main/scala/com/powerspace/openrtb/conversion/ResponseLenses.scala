package com.powerspace.openrtb.conversion

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidResponse
import com.google.openrtb.BidResponse.SeatBid
import monocle.Traversal

object ResponseLenses {
  import SeqTraverse._
  import monocle.function.all._
  import monocle.macros._

  val seatBidLens = GenLens[BidResponse](_.seatbid)
  val bidLens = GenLens[SeatBid](_.bid)

  /** Traversal from req.imps object */
  val seatBidLensTraversal = seatBidLens.composeTraversal(each)
  val bidLensTraversal = bidLens.composeTraversal(each)

  /** Lens composition for req.imp.native.oneOf */
  val bidTraversal: Traversal[BidResponse, SeatBid.Bid] = seatBidLensTraversal.composeTraversal(bidLensTraversal)

  val getAllBids: BidResponse => List[SeatBid.Bid] = bidTraversal.getAll
}
