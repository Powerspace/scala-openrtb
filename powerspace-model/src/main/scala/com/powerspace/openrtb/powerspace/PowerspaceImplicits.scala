package com.powerspace.openrtb.powerspace

import com.google.openrtb.BidRequest
import com.google.openrtb.BidResponse.SeatBid
import com.powerspace.openrtb
import com.powerspace.openrtb.{BidExt, BidRequestExt, PowerspaceProto}

object PowerspaceImplicits {

  implicit class PwsBidRequest(bidRequest: BidRequest) {
    def extension: Option[BidRequestExt] = bidRequest.extension(PowerspaceProto.bidRequest)
    def withExtension(ext: BidRequestExt): BidRequest = bidRequest.withExtension(PowerspaceProto.bidRequest)(Some(ext))
  }

  implicit class PwsBid(bid: SeatBid.Bid) {
    def extension: Option[BidExt] = bid.extension(PowerspaceProto.bid)
    def withExtension(ext: openrtb.BidExt): SeatBid.Bid = bid.withExtension(PowerspaceProto.bid)(Some(ext))
  }

}
