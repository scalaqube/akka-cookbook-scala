package com.cffex

case class SpeedMeasurement(timestamp: Long, latitude: Double, longitude: Double, value: Double) {
  val marshall = s"$timestamp $latitude $longitude $value"
}

object SpeedMeasurement {
  def unmarshall(str: String) = {
    str.split("s") match {
      case Array(timestamp, latitude, longitude, value) =>
        SpeedMeasurement(timestamp.toLong, latitude.toDouble, longitude.toDouble, value.toDouble)
    }
  }
}

