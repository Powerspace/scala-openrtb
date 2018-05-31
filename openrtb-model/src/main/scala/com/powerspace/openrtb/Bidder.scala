package com.powerspace.openrtb

import com.google.openrtb.{BidRequest, BidResponse}
import monix.eval.Task

trait Bidder {
  def bidOn(bidRequest: BidRequest): Task[Option[BidResponse]]
}


