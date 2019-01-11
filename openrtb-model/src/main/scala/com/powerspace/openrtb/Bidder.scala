package com.powerspace.openrtb

import com.google.openrtb.{BidRequest, BidResponse}
import monix.eval.Task

trait Bidder[F[_]] {
  def bidOn(bidRequest: BidRequest): F[Option[BidResponse]]
}


