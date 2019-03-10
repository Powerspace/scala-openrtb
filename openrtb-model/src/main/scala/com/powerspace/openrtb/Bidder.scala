package com.powerspace.openrtb

import com.google.openrtb.{BidRequest, BidResponse}

trait Bidder[F[_]] {
  def bidOn(bidRequest: BidRequest): F[Option[BidResponse]]
}


