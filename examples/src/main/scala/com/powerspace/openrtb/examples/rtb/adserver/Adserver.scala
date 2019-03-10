package com.powerspace.openrtb.examples.rtb.adserver

import java.util.UUID

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof.RequestNative
import com.google.openrtb.NativeRequest.Asset
import com.google.openrtb.NativeRequest.Asset.{AssetOneof, Data, Title}
import com.google.openrtb.{BidRequest, DataAssetType, NativeRequest}
import com.powerspace.openrtb.example.{ExampleProto, ImpExt}

object Adserver {
  def buildBidRequest(): BidRequest = {
    val bidRequestId = UUID.randomUUID().toString

    BidRequest(
      id = bidRequestId,
      imp = Seq(
        Imp(
          id = "1",
          native = Some(
            Native(
              requestOneof = RequestNative(
                NativeRequest(
                  plcmtcnt = Some(1),
                  assets = Seq(
                    Asset(
                      id = 1,
                      required = Some(true),
                      assetOneof = AssetOneof.Title(value = Title(len = 50))
                    )
                  )
                )
              )
            )
          )
        ).withExtension(ExampleProto.impExt)(Some(ImpExt(Some("test"))))
      )
    )
  }
}
