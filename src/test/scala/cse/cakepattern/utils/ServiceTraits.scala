package cse.cakepattern.utils

import java.time.OffsetDateTime

import cse.cakepattern.utils.CaseClasses._

/**
  * Created by dnwiebe on 3/21/17.
  */

object ServiceTraits {
  trait AmmunitionInfoSource {
    def getDetails (ammunition: Ammunition): AmmunitionDetails
  }

  trait WeatherInfoSource {
    def getDetails (location: Location, time: OffsetDateTime): WeatherDetails
  }

  trait Ballistician {
    def computeImpact (
      ammunitionDetails: AmmunitionDetails,
      weatherDetails: WeatherDetails,
      direction: Direction,
      range: Double
    ): Impact
  }
}
