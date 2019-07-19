package com.powerspace.openrtb.benchmarks

import java.util.concurrent.TimeUnit

import com.powerspace.openrtb.json.OpenRtbSerdeModule
import io.circe.Json
import org.openjdk.jmh.annotations._

/**
  * To run the benchmark from within SBT:
  *
  *     jmh:run -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidResponseEncoding
  *
  * Which means "10 iterations", "10 warm-up iterations", "3 forks", "1 thread".
  * Please note that benchmarks should be usually executed at least in
  * 10 iterations (as a rule of thumb), but more is better.
  *
  *
  * It is also possible to:
  *   - record a Flight Recorder for it with
  *
  *       jmh:run -prof jmh.extras.JFR -i 10 -wi 10 -f 3 -t 1 com.powerspace.openrtb.benchmarks.BidResponseEncoding
  *
  *   - generate flame-graphs
  *
  *       jmh:run Bench -i 10 -f 1 -wi 10 -prof jmh.extras.JFR:dir=/tmp/profile-jfr;flameGraphDir=/code/FlameGraph;jfrFlameGraphDir=/code/jfr-flame-graph;flameGraphOpts=--minwidth,2;verbose=true
  *
  * For more details, please refer to https://github.com/ktoso/sbt-jmh
  *
  * Last results:
  * BidResponseEncoding.decodeBidResponseWithExtensions   thrpt   10   6613.921 ±  781.648  ops/s
  * BidResponseEncoding.decodeDefaultBidResponse          thrpt   10  13549.712 ± 1127.012  ops/s
  * BidResponseEncoding.decodeSimpleBidResponse           thrpt   10   8014.099 ±  105.725  ops/s
  *
  */
@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
class BidResponseEncoding {

  import JmhEntities._

  @Benchmark
  def decodeDefaultBidResponse(): Json = {
    OpenRtbSerdeModule.bidResponseEncoder(defaultBidResponse)
  }

  @Benchmark
  def decodeSimpleBidResponse(): Json = {
    OpenRtbSerdeModule.bidResponseEncoder(simpleBidResponse)
  }

  @Benchmark
  def decodeBidResponseWithExtensions(): Json = {
    benchmarkSerdeModule.bidResponseEncoder(bidResponseWithExtensions)
  }
}
