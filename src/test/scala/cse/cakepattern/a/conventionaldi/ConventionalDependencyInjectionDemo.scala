package cse.cakepattern.a.conventionaldi

import java.time.OffsetDateTime

import cse.cakepattern.utils.CaseClasses._
import cse.cakepattern.utils.ServiceTraits._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.{ArgumentCaptor, Matchers}
import org.scalatest.path

/**
  * Created by dnwiebe on 3/19/17.
  */

object ConventionalDependencyInjectionDemo {

  class Rifle (ais: AmmunitionInfoSource, wis: WeatherInfoSource, b: Ballistician) {
    def fire (ammunition: Ammunition, location: Location, direction: Direction, range: Double): Impact = {
      val ammunitionDetails = ais.getDetails (ammunition)
      val weatherDetails = wis.getDetails (location, OffsetDateTime.now ())
      b.computeImpact (ammunitionDetails, weatherDetails, direction, range)
    }
  }

  //////////////////////////////////////////////////////////////////////////////

  class SAAMIAmmunitionInfoSource extends AmmunitionInfoSource {
    def getDetails (ammunition: Ammunition): AmmunitionDetails = {
      null // not really
    }
  }

  class NWSWeatherInfoSource extends WeatherInfoSource {
    def getDetails (location: Location, time: OffsetDateTime): WeatherDetails = {
      null // not really
    }
  }

  class SiacciBallistician extends Ballistician {
    def computeImpact (ad: AmmunitionDetails, sw: WeatherDetails, d: Direction, range: Double): Impact = {
      null // not really
    }
  }

  val rifle = new Rifle (new SAAMIAmmunitionInfoSource (), new NWSWeatherInfoSource (), new SiacciBallistician ())
}

class ConventionalDependencyInjectionDemo extends path.FunSpec {
  import ConventionalDependencyInjectionDemo._

  describe ("A Rifle, given appropriate mocks") {
    val ammunitionInfoSource = mock (classOf[AmmunitionInfoSource])
    val weatherInfoSource = mock (classOf[WeatherInfoSource])
    val ballistician = mock (classOf[Ballistician])
    val subject = new Rifle (ammunitionInfoSource, weatherInfoSource, ballistician)

    describe ("outfitted for a medium-range westward shot in Colorado") {
      val ammunition = Ammunition ("Hornady", "Superformance", "Win 308 165gr Interbond")
      val ammunitionDetails = AmmunitionDetails (165.0, 0.523, 866.0)
      val location = Location (38.820046, -106.378879)
      val wind = Wind (293.0, 1.38)
      val weatherDetails = WeatherDetails (10.7, 29.92, 20.4, wind)
      val direction = Direction (273.0, -5.33)
      val range = 306.0
      val impact = Impact (-0.00294, -0.193)

      when (ammunitionInfoSource.getDetails(Ammunition ("Hornady", "Superformance", "Win 308 165gr Interbond")))
        .thenReturn (ammunitionDetails)
      when (weatherInfoSource.getDetails (Matchers.eq (location), any (classOf[OffsetDateTime])))
        .thenReturn (weatherDetails)
      when (ballistician.computeImpact (ammunitionDetails, weatherDetails, direction, range))
        .thenReturn (impact)

      describe ("when fired") {
        val startTime = OffsetDateTime.now ()
        val result = subject.fire (ammunition, location, direction, range)
        val endTime = OffsetDateTime.now ()

        it ("prints at the expected offset from point-of-aim") {
          assert (result === impact)
        }

        it ("passes in the correct time") {
          val captor = ArgumentCaptor.forClass (classOf[OffsetDateTime])
          verify (weatherInfoSource).getDetails (any (classOf[Location]), captor.capture())
          val actualTime = captor.getValue
          assert (startTime.isBefore (actualTime) || startTime.isEqual (actualTime))
          assert (endTime.isAfter (actualTime) || endTime.isEqual (actualTime))
        }
      }
    }
  }
}
