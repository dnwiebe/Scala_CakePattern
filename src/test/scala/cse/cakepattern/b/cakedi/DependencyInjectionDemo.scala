package cse.cakepattern.b.cakedi

import java.time.OffsetDateTime

import cse.cakepattern.utils.CaseClasses._
import cse.cakepattern.utils.ServiceTraits.{AmmunitionInfoSource, Ballistician, WeatherInfoSource}
import org.scalatest.path

/**
  * Created by dnwiebe on 3/19/17.
  */
object DependencyInjectionDemo {

  // Note: trait, not class
  trait Rifle {
    this: AmmunitionInfoSource with WeatherInfoSource with Ballistician =>
    def fire (a: Ammunition, l: Location, d: Direction, range: Double): Impact = {
      // Note: getDetails, not ais.getDetails
      val ammunitionDetails = getDetails (a)
      // Note: getDetails, not wis.getDetails
      val weatherDetails = getDetails (l, OffsetDateTime.now ())
      // Note: computeImpact, not b.computeImpact
      computeImpact (ammunitionDetails, weatherDetails, d, range)
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////

  // Note: traits, not classes
  trait RealAmmunitionInfoSource extends AmmunitionInfoSource {
    def getDetails (ammunition: Ammunition): AmmunitionDetails = {
      null // not really
    }
  }

  trait RealWeatherInfoSource extends WeatherInfoSource {
    def getDetails (location: Location, time: OffsetDateTime): WeatherDetails = {
      null // not really
    }
  }

  trait RealBallistician extends Ballistician {
    def computeImpact (ad: AmmunitionDetails, sw: WeatherDetails, d: Direction, range: Double): Impact = {
      null // not really
    }
  }

  val rifle = new Rifle with RealAmmunitionInfoSource with RealWeatherInfoSource with RealBallistician
}

class DependencyInjectionDemo extends path.FunSpec {
  import cse.cakepattern.b.cakedi.DependencyInjectionDemo._

  val ammunitionDetails = AmmunitionDetails (165.0, 0.523, 866.0)
  val weatherDetails = WeatherDetails (10.7, 20.4, Wind (293.0, 1.38))
  val impact = Impact (-0.00294, -0.193)

  private var recording = Map[String, Any] ()

  trait MockAmmunitionInfoSource extends AmmunitionInfoSource {
    override def getDetails (ammunition: Ammunition): AmmunitionDetails = {
      recording ++= Map ("getDetails.ammunition" -> ammunition)
      ammunitionDetails
    }
  }

  trait MockWeatherInfoSource extends WeatherInfoSource {
    override def getDetails (location: Location, time: OffsetDateTime): WeatherDetails = {
      recording ++= Map ("getDetails.location" -> location, "getDetails.time" -> time)
      weatherDetails
    }
  }

  trait MockBallistician extends Ballistician {
    override def computeImpact (
      ammunitionDetails: AmmunitionDetails,
      weatherDetails: WeatherDetails,
      direction: Direction,
      range: Double
    ): Impact = {
      recording ++= Map (
        "computeImpact.ammunitionDetails" -> ammunitionDetails,
        "computeImpact.weatherDetails" -> weatherDetails,
        "computeImpact.direction" -> direction,
        "computeImpact.range" -> range
      )
      impact
    }
  }

  describe ("A rifle, created with mocked dependencies") {
    val subject = new Rifle () with MockAmmunitionInfoSource with MockWeatherInfoSource with MockBallistician

    describe ("outfitted for a medium-range westward shot in Colorado") {
      val ammunition = Ammunition ("Hornady", "Superformance", "Win 308 165gr Interbond")
      val location = Location (38.820046, -106.378879)
      val direction = Direction (273.0, -5.33)
      val range = 306.0

      describe ("when fired") {
        val startTime = OffsetDateTime.now ()
        val result = subject.fire (ammunition, location, direction, range)
        val endTime = OffsetDateTime.now ()

        it ("prints at the expected offset from point-of-aim") {
          assert (result === impact)
        }

        it ("calls AmmunitionInfoSource correctly") {
          assert (recording ("getDetails.ammunition") === ammunition)
        }

        it ("calls WeatherInfoSource correctly") {
          assert (recording ("getDetails.location") === location)
          val actualTime = recording ("getDetails.time").asInstanceOf[OffsetDateTime]
          assert (startTime.isBefore (actualTime) || startTime.isEqual (actualTime))
          assert (endTime.isAfter (actualTime) || endTime.isEqual (actualTime))
        }

        it ("calls Ballistician correctly") {
          assert (recording ("computeImpact.ammunitionDetails") === ammunitionDetails)
          assert (recording ("computeImpact.weatherDetails") === weatherDetails)
          assert (recording ("computeImpact.direction") === direction)
          assert (recording ("computeImpact.range") === range)
        }
      }
    }
  }
}
