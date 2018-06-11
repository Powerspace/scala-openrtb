package com.powerspace.openrtb.json

import io.circe.Encoder

trait EncoderProvider[T] {
  def encoder : Encoder[T]
}
