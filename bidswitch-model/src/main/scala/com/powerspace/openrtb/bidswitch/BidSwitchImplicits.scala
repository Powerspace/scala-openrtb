package com.powerspace.openrtb.bidswitch

import com.google.openrtb.BidResponse.SeatBid
import com.google.openrtb.{BidRequest, BidResponse}
import com.powerspace.bidswitch.{BidExt, BidRequestExt, BidResponseExt, BidswitchProto}

object BidSwitchImplicits {

  implicit class BswBidRequest(bidRequest: BidRequest) {
    def extension: Option[BidRequestExt] = bidRequest.extension(BidswitchProto.bidRequestExt)

    def withExtension(ext: BidRequestExt): BidRequest =
      bidRequest.withExtension(BidswitchProto.bidRequestExt)(Some(ext))
  }

  implicit class BswBidResponse(bidResponse: BidResponse) {
    def extension: Option[BidResponseExt] = bidResponse.extension(BidswitchProto.bidResponseExt)

    def withExtension(ext: BidResponseExt): BidResponse =
      bidResponse.withExtension(BidswitchProto.bidResponseExt)(Some(ext))
  }

  implicit class BswBid(bid: SeatBid.Bid) {
    def extension: Option[BidExt] = bid.extension(BidswitchProto.bidExt)
    def withExtension(ext: BidExt): SeatBid.Bid = bid.withExtension(BidswitchProto.bidExt)(Some(ext))
  }

}
