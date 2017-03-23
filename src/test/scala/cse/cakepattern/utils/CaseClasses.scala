package cse.cakepattern.utils

/**
  * Created by dnwiebe on 3/19/17.
  */

object CaseClasses {

  case class Ammunition (brand: String, name: String, chambering: String)

  case class Location (latitude: Double, longitude: Double)

  case class Impact (right: Double, high: Double)

  case class Direction (azimuth: Double, elevation: Double)

  case class Wind (fromAzimuth: Double, speed: Double)

  case class AmmunitionDetails (bulletWeight: Double, ballisticCoefficient: Double, muzzleVelocity: Double)

  case class WeatherDetails (temperature: Double, humidity: Double, wind: Wind)
}
