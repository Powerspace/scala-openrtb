package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.bidswitch.{BidExt, BidRequestExt, BidResponseExt, BidswitchProto}

object BidSwitchImplicits {
  implicit class BswBidRequest(bidRequest: BidRequest) {
    def bsw: Option[BidRequestExt] = bidRequest.extension(BidswitchProto.bidRequestExt)
    def withExt(ext: BidRequestExt) = bidRequest.withExtension(BidswitchProto.bidRequestExt)(Some(ext))
  }

  implicit class BswBidResponse(bidResponse: BidResponse) {
    def bsw: Option[BidResponseExt] = bidResponse.extension(BidswitchProto.bidResponseExt)
    def withExt(ext: BidResponseExt) = bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(ext))
  }

  implicit class BswBid(bid: SeatBid.Bid) {
    def bsw: Option[BidExt] = bid.extension(BidswitchProto.bidExt)
    def withExt(ext: BidExt) = bid.withExtension(BidswitchProto.bidExt)(Some(ext))
  }
}


