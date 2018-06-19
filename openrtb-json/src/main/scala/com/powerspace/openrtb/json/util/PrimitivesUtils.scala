package com.powerspace.openrtb.json.util

object PrimitivesUtils {

  /**
    * Convert a boolean to the related integer value
    */
  implicit class BooleanEnhancement(b: Boolean) {
    def toInt: Int = if (b) 1 else 0
  }

  /**
    * Convert an integer to the related boolean value
    */
  implicit class IntEnhancement(i: Int) {
    def toBoolean: Boolean = if (i == 0) false else true
  }

}
