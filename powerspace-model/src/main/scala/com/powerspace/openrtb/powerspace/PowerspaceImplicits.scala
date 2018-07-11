package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidRequest
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb
import com.powerspace.openrtb.{BidRequestExt, PowerspaceProto}

object PowerspaceImplicits {

  implicit class PwsBidRequest(bidRequest: BidRequest) {
    def extension = bidRequest.extension(PowerspaceProto.bidRequest)
    def withExtension(ext: BidRequestExt) = bidRequest.withExtension(PowerspaceProto.bidRequest)(Some(ext))
  }

  implicit class PwsBid(bid: SeatBid.Bid) {
    def extension = bid.extension(PowerspaceProto.bid)
    def withExtension(ext: openrtb.BidExt) = bid.withExtension(PowerspaceProto.bid)(Some(ext))
  }

}
