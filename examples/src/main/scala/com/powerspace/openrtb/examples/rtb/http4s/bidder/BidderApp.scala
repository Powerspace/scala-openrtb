package com.powerspace.openrtb.examples.rtb.http4s.bidder

import cats.effect.ExitCode
import monix.eval.Task

object BidderApp extends App {

  import monix.execution.Scheduler.Implicits.global

  stream.compile.drain.runAsyncAndForget

  /**
    * Bind an http service
    */
  def stream: fs2.Stream[Task, ExitCode] = {
    import org.http4s.server.blaze._

    BlazeServerBuilder[Task]
      .bindHttp(port = 9000, host = "localhost")
      .withHttpApp(BidderHttpAppBuilder.build())
      .serve
  }
}
