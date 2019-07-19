package com.powerspace.openrtb.benchmarks

import java.util.concurrent.TimeUnit

import com.google.openrtb.BidResponse
import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.Decoder.Result
import io.circe.Json
import org.openjdk.jmh.annotations._

/**
  * To run the benchmark from within SBT:
  *
  *     jmh:run -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidResponseDecoding
  *
  * Which means "10 iterations", "10 warm-up iterations", "3 forks", "1 thread".
  * Please note that benchmarks should be usually executed at least in
  * 10 iterations (as a rule of thumb), but more is better.
  *
  *
  * It is also possible to:
  *   - record a Flight Recorder for it with
  *
  *       jmh:run -prof jmh.extras.JFR -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidResponseDecoding
  *
  *   - generate flame-graphs
  *
  *       jmh:run Bench -i 10 -f 1 -wi 10 -prof jmh.extras.JFR:dir=/tmp/profile-jfr;flameGraphDir=/code/FlameGraph;jfrFlameGraphDir=/code/jfr-flame-graph;flameGraphOpts=--minwidth,2;verbose=true
  *
  * For more details, please refer to https://github.com/ktoso/sbt-jmh
  *
  * Last results:
  * BidResponseDecoding.decodeBidResponseWithExtensions  thrpt   10   7242.017 ± 2547.405  ops/s
  * BidResponseDecoding.decodeDefaultBidResponse         thrpt   10   8445.170 ± 1614.318  ops/s
  * BidResponseDecoding.decodeSimpleBidResponse          thrpt   10   8992.772 ± 1439.870  ops/s
  *
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BidResponseDecoding {

  import JmhEntities._

  val defaultBidResponseJson: Json = OpenRtbSerdeModule.bidResponseEncoder(defaultBidResponse)

  val simpleBidResponseJson: Json = OpenRtbSerdeModule.bidResponseEncoder(simpleBidResponse)

  val bidResponseWithExtensionsJson: Json = OpenRtbSerdeModule.bidResponseEncoder(bidResponseWithExtensions)

  @Benchmark
  def decodeDefaultBidResponse(): Result[BidResponse] = {
    OpenRtbSerdeModule.bidResponseDecoder.decodeJson(defaultBidResponseJson)
  }

  @Benchmark
  def decodeSimpleBidResponse(): Result[BidResponse] = {
    OpenRtbSerdeModule.bidResponseDecoder.decodeJson(simpleBidResponseJson)
  }

  @Benchmark
  def decodeBidResponsetWithExtensions(): Result[BidResponse] = {
    benchmarkSerdeModule.bidResponseDecoder.decodeJson(bidResponseWithExtensionsJson)
  }

}
