package com.powerspace.openrtb

import com.powerspace.bidswitch.BidswitchProto
import com.powerspace.openrtb.bidswitch.BidSwitchSerdeModule
import com.powerspace.openrtb.conversion.ResponseLenses
import org.scalatest.{FunSuite, GivenWhenThen, Ignore}

class ExtensionTest extends FunSuite with GivenWhenThen {

  test("Automatic encoder builder") {
    val serde = /*PowerspaceSerdeModule + */BidSwitchSerdeModule

    //val addExt: BidResponse => BidResponse = ResponseLenses.bidTraversal.modify(bid => bid.withExtension(PowerspaceProto.bid)(Some(BidExt(ctr = Some(1.0d)))))

    val incomingBidResponse = com.powerspace.openrtb.bidswitch.fixtures.BidResponseFixtures.sampleBidResponse(true)

    val responseJson = serde.bidResponseEncoder(incomingBidResponse)
    val responseObject = serde.bidResponseDecoder.decodeJson(responseJson).toOption.get

    val bidResponseExtension = responseObject.extension(BidswitchProto.bidResponseExt).get
    assert(bidResponseExtension.protocol.contains("protocol-1"))

    val bidResponse = ResponseLenses.bidTraversal.headOption(responseObject)

    //val pwsExt = bidResponse.flatMap(_.extension(PowerspaceProto.bid)).get
    //assert(pwsExt.ctr.isDefined)

    val bswExt = bidResponse.flatMap(_.extension(BidswitchProto.bidExt)).get
    assert(bswExt.google.isDefined)
  }

}
