package com.powerspace.openrtb.benchmarks

import java.util.concurrent.TimeUnit

import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.Json
import org.openjdk.jmh.annotations._

/**
  * To run the benchmark from within SBT:
  *
  *     jmh:run -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidRequestEncoding
  *
  * Which means "10 iterations", "10 warm-up iterations", "3 forks", "1 thread".
  * Please note that benchmarks should be usually executed at least in
  * 10 iterations (as a rule of thumb), but more is better.
  *
  *
  * It is also possible to:
  *   - record a Flight Recorder for it with
  *
  *       jmh:run -prof jmh.extras.JFR -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidRequestEncoding
  *
  *   - generate flame-graphs
  *
  *       jmh:run Bench -i 10 -f 1 -wi 10 -prof jmh.extras.JFR:dir=/tmp/profile-jfr;flameGraphDir=/code/FlameGraph;jfrFlameGraphDir=/code/jfr-flame-graph;flameGraphOpts=--minwidth,2;verbose=true
  *
  * For more details, please refer to https://github.com/ktoso/sbt-jmh
  *
  * Last results:
  * BidRequestEncoding.encodeBidRequestWithExtensions   thrpt   10    828.487 ±   41.169  ops/s
  * BidRequestEncoding.encodeDefaultBidRequest          thrpt   10    930.089 ±   61.319  ops/s
  * BidRequestEncoding.encodeSimpleBidRequest           thrpt   10    887.830 ±   54.475  ops/s
  *
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BidRequestEncoding {

  import JmhEntities._

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
