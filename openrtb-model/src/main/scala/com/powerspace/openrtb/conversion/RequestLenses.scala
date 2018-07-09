package com.powerspace.openrtb.conversion

import com.google.openrtb.{BidRequest, BidResponse}
import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidResponse.SeatBid
import monocle.{PTraversal, Traversal}

object RequestLenses {
  import SeqTraverse._
  import monocle.function.all._
  import monocle.macros._
  import monocle.std.all._

  val native = GenLens[Imp](_.native)
  val nativeRequestOneOf = GenLens[Native](_.requestOneof)
  val impLens = GenLens[BidRequest](_.imp)

  /** Traversal from req.imps object */
  val impsTraversal = impLens.composeTraversal(each)

  /** Optional from imp.native object */
  val optNative = native.composePrism(some)

  /** Lens composition for req.imp.native.oneOf */
  val nativeRequestOneOfTraversal = impsTraversal.composeTraversal(optNative
    .composeOptional(nativeRequestOneOf.asOptional).asTraversal)

  val getAllNatives: BidRequest => List[RequestOneof] = nativeRequestOneOfTraversal
    .getAll
}



