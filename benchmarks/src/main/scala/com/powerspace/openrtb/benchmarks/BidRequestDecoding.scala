package com.powerspace.openrtb.benchmarks

import java.util.concurrent.TimeUnit

import com.google.openrtb.BidRequest
import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.Decoder.Result
import io.circe.Json
import org.openjdk.jmh.annotations._

/**
  * To run the benchmark from within SBT:
  *
  *     jmh:run -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidRequestDecoding
  *
  * Which means "10 iterations", "10 warm-up iterations", "3 forks", "1 thread".
  * Please note that benchmarks should be usually executed at least in
  * 10 iterations (as a rule of thumb), but more is better.
  *
  *
  * It is also possible to:
  *   - record a Flight Recorder for it with
  *
  *       jmh:run -prof jmh.extras.JFR -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidRequestDecoding
  *
  *   - generate flame-graphs
  *
  *       jmh:run Bench -i 10 -f 1 -wi 10 -prof jmh.extras.JFR:dir=/tmp/profile-jfr;flameGraphDir=/code/FlameGraph;jfrFlameGraphDir=/code/jfr-flame-graph;flameGraphOpts=--minwidth,2;verbose=true
  *
  * For more details, please refer to https://github.com/ktoso/sbt-jmh
  *
  * Last results:
  * BidRequestDecoding.decodeBidRequestWithExtensions   thrpt   10    817.812 ±   14.980  ops/s
  * BidRequestDecoding.decodeDefaultBidRequest          thrpt   10    915.832 ±   42.983  ops/s
  * BidRequestDecoding.decodeSimpleBidRequest           thrpt   10    842.699 ±   62.196  ops/s
  *
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BidRequestDecoding {

  import JmhEntities._

  val defaultBidRequestJson: Json = OpenRtbSerdeModule.bidRequestEncoder(defaultBidRequest)

  val simpleBidRequestJson: Json = OpenRtbSerdeModule.bidRequestEncoder(simpleBidRequest)

  val bidRequestWithExtensionsJson: Json = OpenRtbSerdeModule.bidRequestEncoder(bidRequestWithExtensions)

  @Benchmark
  def decodeDefaultBidRequest(): Result[BidRequest] = {
    OpenRtbSerdeModule.bidRequestDecoder.decodeJson(defaultBidRequestJson)
  }

  @Benchmark
  def decodeSimpleBidRequest(): Result[BidRequest] = {
    OpenRtbSerdeModule.bidRequestDecoder.decodeJson(simpleBidRequestJson)
  }

  @Benchmark
  def decodeBidRequestWithExtensions(): Result[BidRequest] = {
    benchmarkSerdeModule.bidRequestDecoder.decodeJson(bidRequestWithExtensionsJson)
  }

}
