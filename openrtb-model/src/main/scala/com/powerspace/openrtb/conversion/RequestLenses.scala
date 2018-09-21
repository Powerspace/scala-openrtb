package com.powerspace.openrtb.conversion

import com.google.openrtb.BidRequest.Imp
import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.NativeRequest.Asset
import com.google.openrtb.{BidRequest, NativeRequest}
import monocle._

object RequestLenses {

  import SeqTraverse._
  import monocle.function.all._
  import monocle.macros._
  import monocle.std.all._

  /**
    * Single lens
    */
  val bidRequestToImp: PTraversal[BidRequest, BidRequest, Imp, Imp] = GenLens[BidRequest](_.imp).composeTraversal(each)
  val impToNative = GenLens[Imp](_.native).composePrism(some)
  val nativeToRequestOneOf = GenLens[Native](_.requestOneof)

  val requestOneOfToNativeRequest = GenLens[RequestOneof](_.requestNative).composePrism(some)

  val nativeRequestToAsset = GenLens[NativeRequest](_.assets).composeTraversal(each)
  val assetToAssetOneOf = GenLens[NativeRequest.Asset](_.assetOneof)
  val assetOneOfToData = GenLens[Asset.AssetOneof](_.data).composePrism(some)

  val bidRequestToUser = GenLens[BidRequest](_.user).composePrism(some)

  val bidRequestToDistributionChannelOneOf = GenLens[BidRequest](_.distributionchannelOneof)
  val distributionChannelOneOfToSite = GenLens[BidRequest.DistributionchannelOneof](_.site).composePrism(some)

  /**
    * Compositions
    */
  val bidRequestToRequestOneOf: Traversal[BidRequest, RequestOneof] = bidRequestToImp
    .composeTraversal(impToNative.
      composeOptional(nativeToRequestOneOf.asOptional)
      .asTraversal
    )

  val bidRequestToNative: Traversal[BidRequest, Native] = bidRequestToImp
    .composeOptional(impToNative)

  val nativeToNativeRequest: Optional[Native, NativeRequest] = nativeToRequestOneOf
    .composeOptional(requestOneOfToNativeRequest)

  val bidRequestToNativeRequest: Traversal[BidRequest, NativeRequest] =
    bidRequestToNative.composeOptional(nativeToNativeRequest)

  val nativeToDataTraversal: Traversal[Native, Asset.Data] = nativeToNativeRequest
    .composeTraversal(nativeRequestToAsset)
    .composeLens(assetToAssetOneOf)
    .composeOptional(assetOneOfToData)

  val bidRequestToSite: Optional[BidRequest, BidRequest.Site] = bidRequestToDistributionChannelOneOf
    .composeOptional(distributionChannelOneOfToSite)

}



