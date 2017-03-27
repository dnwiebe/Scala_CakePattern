package cse.cakepattern.c.clumsiness

import org.scalatest.path

/**
  * Created by dnwiebe on 3/21/17.
  */

object UpperClumsyDemo {

  trait Engine {
    def setThrottle (rpm: Double): Unit
    def setMixture (fuelToAirByWeight: Double): Unit
    def exhaustGasTemperature: Double
    def engineTemperature: Double
  }

  trait Propeller {
    def setIncidenceAngle (position: Double)
  }

  trait Instruments {
    def indicatedAirspeed: Double
    def pitchAngle: Double
    def rollAngle: Double
    def altitude: Double
    def yawRate: Double
    def inclination: Double
    def whiskeyCompass: Double
    def indicatedDirection: Double
    def verticalSpeed: Double
  }

  trait Fadec {
    this: Engine with Propeller with Instruments =>

    def setPower (value: Double): Unit = {
      // Can control throttle, mixture, and propeller and sense airspeed--with this: collisions?
      setThrottle (0.0)
      setMixture (0.0)
      setIncidenceAngle (0.0)
      val airspeed = indicatedAirspeed
    }
  }

  trait FlightControls {
    def setPitchPressure (pitch: Double)
    def setRoll (yokePosition: Double)
    def setYawPressure (yaw: Double)
  }

  trait Airplane {
    this: Fadec with Instruments with FlightControls =>

    def method (): Unit = {
      // can control Fadec
      setPower (0.0)
      // can control flight
      setPitchPressure (0.0)
      setRoll (0.0)
      setYawPressure (0.0)
      // can read instruments
      val airspeed = indicatedAirspeed
      val altitudeValue = altitude

      // cannot control throttle or mixture or propeller
//      setThrottle (0.0)
//      setMixture (0.0)
//      setIncidenceAngle (0.0)
    }
  }
}

object LowerClumsyDemo {

  trait EngineComponent {
    val engine: Engine

    trait Engine {
      def setThrottle (rpm: Double): Unit
      def setMixture (fuelToAirByWeight: Double): Unit
      def exhaustGasTemperature: Double
      def engineTemperature: Double
    }
  }

  trait PropellerComponent {
    val propeller: Propeller

    trait Propeller {
      def setIncidenceAngle (position: Double)
    }
  }

  trait InstrumentsComponent {
    val instruments: Instruments

    trait Instruments {
      def indicatedAirspeed: Double
      def pitchAngle: Double
      def rollAngle: Double
      def altitude: Double
      def yawRate: Double
      def inclination: Double
      def whiskeyCompass: Double
      def indicatedDirection: Double
      def verticalSpeed: Double
    }
  }

  trait FadecComponent {
    val fadec: Fadec

    trait Fadec {
      this: EngineComponent with PropellerComponent with InstrumentsComponent =>

      def setPower (value: Double): Unit = {
        // Can control throttle, mixture, and propeller and sense airspeed
        engine.setThrottle (0.0)
        engine.setMixture (0.0)
        propeller.setIncidenceAngle (0.0)
        val airspeed = instruments.indicatedAirspeed
      }
    }
  }

  trait FlightControlsComponent {
    val flightControls: FlightControls

    trait FlightControls {
      def setPitchPressure (pitch: Double)
      def setRoll (yokePosition: Double)
      def setYawPressure (yaw: Double)
    }
  }

  trait Airplane {
    this: FadecComponent with InstrumentsComponent with FlightControlsComponent =>

    def method (): Unit = {
      // can control Fadec
      fadec.setPower (0.0)
      // can control flight
      flightControls.setPitchPressure (0.0)
      flightControls.setRoll (0.0)
      flightControls.setYawPressure (0.0)
      // can read instruments
      val airspeed = instruments.indicatedAirspeed
      val altitude = instruments.altitude

      // cannot control throttle or mixture or propeller
//      fadec.engine.setThrottle (0.0)
//      fadec.engine.setMixture (0.0)
//      fadec.propeller.setIncidenceAngle (0.0)
    }
  }

}

object MultipleClumsinessDemoNo {

  trait Fadec {
    this: EngineComponent with EngineComponent =>
  }

  trait EngineComponent {
    val engine: Engine

    trait Engine {
      val position: String
    }
  }

  trait LeftEngineComponent extends EngineComponent {
    override val engine = new Engine () {override val position = "Left"}
  }
  trait RightEngineComponent extends EngineComponent {
    override val engine = new Engine () {override val position = "Right"}
  }
}

class MultipleClumsinessDemoNo extends path.FunSpec {
  import MultipleClumsinessDemoNo._

  describe ("A Fadec with two engines") {
    val fadec = new Fadec with LeftEngineComponent with RightEngineComponent

    describe ("when asked about its engines") {
      val result = fadec.engine.position

      it ("really only has the last one specified") {
        assert (result === "Right")
      }
    }
  }
}

object MultipleClumsinessDemoYes {

  trait Fadec {
    this: MultiEngineComponent =>
  }

  trait MultiEngineComponent {
    val leftEngine: Engine
    val rightEngine: Engine

    trait Engine {
      val position: String
    }
  }

  trait RealMultiEngineComponent extends MultiEngineComponent {
    val leftEngine = new Engine () {override val position = "Left"}
    val rightEngine = new Engine () {override val position = "Right"}
  }
}
