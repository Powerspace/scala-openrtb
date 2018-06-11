package com.powerspace.openrtb.json

import io.circe.Encoder
import io.circe.generic.extras.Configuration

trait EncoderProvider[T] {
  protected implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  def encoder : Encoder[T]
}
