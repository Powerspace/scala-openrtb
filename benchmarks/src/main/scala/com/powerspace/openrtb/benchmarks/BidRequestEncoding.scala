package com.powerspace.openrtb.benchmarks

import java.util.UUID
import java.util.concurrent.TimeUnit

import com.google.openrtb.BidRequest.Imp.Native
import com.google.openrtb.BidRequest.Imp.Native.RequestOneof
import com.google.openrtb.BidRequest.{Geo, Imp, User}
import com.google.openrtb.{BidRequest, ContextType, NativeRequest, PlacementType}
import com.powerspace.openrtb.benchmarks.serde.BenchmarkSerdeModule
import com.powerspace.openrtb.example.{ExampleProto, ImpExt}
import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.Json
import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BidRequestEncoding {

  val defaultBidRequest: BidRequest = BidRequest.defaultInstance

  val simpleBidRequest: BidRequest = BidRequest(
    id = UUID.randomUUID().toString,
    wseat = Seq("seat-1", "seat-2", "seat-3"),
    cur = Seq("EUR", "USD"),
    test = None,
    user = Some(
      User(
        id = Some(UUID.randomUUID().toString),
        geo = Some(
          Geo(
            lat = Some(48.8566),
            lon = Some(2.3522),
            country = Some("fr")
          ))
      )),
    imp = Seq(
      Imp(
        id = "1",
        native = Some(
          Native(
            requestOneof = RequestOneof.RequestNative(
              NativeRequest(
                plcmtcnt = Some(5),
                ver = Some("1"),
                context = Some(ContextType.CONTENT),
                plcmttype = Some(PlacementType.IN_FEED)
              )
            )
          ))
      ))
  )

  val bidRequestWithExtensions: BidRequest = simpleBidRequest.copy(
    imp = simpleBidRequest.imp
      .map(
        _.withExtension(ExampleProto.impExt)(
          Some(ImpExt(Some("test-extension")))
        ))
  )

  val benchmarkSerdeModule = new BenchmarkSerdeModule

  @Benchmark
  def encodeDefaultBidRequest(): Json = {
    OpenRtbSerdeModule.bidRequestEncoder(defaultBidRequest)
  }

  @Benchmark
  def encodeSimpleBidRequest(): Json = {
    OpenRtbSerdeModule.bidRequestEncoder(simpleBidRequest)
  }

  @Benchmark
  def encodeBidRequestWithExtensions(): Json = {
    benchmarkSerdeModule.bidRequestEncoder(bidRequestWithExtensions)
  }

}
