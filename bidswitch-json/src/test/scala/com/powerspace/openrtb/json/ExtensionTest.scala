package com.powerspace.openrtb.json

import com.google.openrtb.BidResponse
import com.powerspace.openrtb.bidswitch.BidSwitchSerdeModule
import com.powerspace.openrtb.conversion.ResponseLenses
import com.powerspace.openrtb.powerspace.PowerspaceSerdeModule
import com.powerspace.openrtb.{BidExt, PowerspaceProto}
import org.scalatest.{FunSuite, GivenWhenThen}

class ExtensionTest extends FunSuite with GivenWhenThen {
  import com.powerspace.openrtb.bidswitch.BidSwitchImplicits._
  import com.powerspace.openrtb.powerspace.PowerspaceImplicits._

  test("Automatic encoder builder") {
    val serDe = PowerspaceSerdeModule + BidSwitchSerdeModule
    val addExt: BidResponse => BidResponse = ResponseLenses.bidTraversal.modify(bid => bid.withExtension(PowerspaceProto.bid)(Some(BidExt(ctr = Some(1.0d)))))

    val incomingBidResponse = addExt(com.powerspace.openrtb.bidswitch.BidResponseFixtures.sampleBidResponse(true))

    val responseJson = serDe.bidResponseEncoder(incomingBidResponse)
    val possibleResp = serDe.bidResponseDecoder.decodeJson(responseJson)

    val outcomingBidResponse = possibleResp.toOption.get
    val bidResponseExtension = outcomingBidResponse.bsw.get
    assert(bidResponseExtension.protocol.contains("protocol-1"))

    ResponseLenses.bidTraversal.headOption(outcomingBidResponse)

    val bswExt = ResponseLenses.bidTraversal.headOption(outcomingBidResponse).flatMap(_.bsw).get
    assert(bswExt.google.isDefined)

    val pwsExt = ResponseLenses.bidTraversal.headOption(outcomingBidResponse).flatMap(_.pws).get
    assert(pwsExt.ctr.isDefined)
  }

}
