package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.openrtb
import com.powerspace.openrtb.{BidRequestExt, PowerspaceProto}

object PowerspaceImplicits {
  implicit class PwsBidRequest(bidRequest: BidRequest) {
    def pws: Option[BidRequestExt] = bidRequest.extension(PowerspaceProto.bidRequest)
    def withExt(ext: BidRequestExt) = bidRequest.withExtension(PowerspaceProto.bidRequest)(Some(ext))
  }

  implicit class PwsBid(bid: SeatBid.Bid) {
    def pws: Option[openrtb.BidExt] = bid.extension(PowerspaceProto.bid)
    def withExt(ext: openrtb.BidExt) = bid.withExtension(PowerspaceProto.bid)(Some(ext))
  }
}
