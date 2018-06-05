package com.powerspace.openrtb.bidswitch.util

import io.circe.Json

object JsonUtils {

  import io.circe.syntax._

  implicit class JsonEnhancement(json: Json) {
    def addExtension(extension: Json): Json = json.asObject.get.add("ext", extension).asJson
  }

}
