package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidRequest
import com.powerspace.bidswitch.{BidRequestExt, BidswitchProto}

object BidswitchFactory {
  def apply(bidRequest: BidRequest, ext: BidRequestExt): BidRequest = {
    bidRequest.withExtension(BidswitchProto.bidRequestExt)(Some(ext))
  }
}