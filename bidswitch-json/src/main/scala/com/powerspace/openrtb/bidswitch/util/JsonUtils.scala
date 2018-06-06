package com.powerspace.openrtb.bidswitch.util

import io.circe.Json

object JsonUtils {

  import io.circe.syntax._

  /**
    * Add an RTB extension to the JSON object
    */
  implicit class JsonEnhancement(json: Json) {
    def addExtension(extension: Json): Json = json.asObject.get.add("ext", extension).asJson
  }

}
