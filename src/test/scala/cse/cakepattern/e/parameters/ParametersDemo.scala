package cse.cakepattern.e.parameters

import org.scalatest.path

/**
  * Created by dnwiebe on 3/22/17.
  */

object ParametersDemo {

  trait CakeElectronicKeyboard {
    this: Keyboard with PowerAmp with Speaker =>

    // functionality here
  }

  trait Keyboard {
    val keyCount: Int
    // imagine everything else specified
  }

  trait PowerAmp {
    val outputPower: Int
    // imagine everything else specified
  }

  trait Speaker {
    val impedance: Int
    val diameter: Int
    // imagine everything else specified
  }

  ////////////////////////////////////////

  trait Keyboard49 extends Keyboard {override val keyCount = 49}
  trait PowerAmp50 extends PowerAmp {override val outputPower = 50}
  trait Speaker8Ω10ˮ extends Speaker {override val impedance = 8; override val diameter = 10}

  val cakeElectronicKeyboard = new CakeElectronicKeyboard with Keyboard49 with PowerAmp50 with Speaker8Ω10ˮ

  ////////////////////////////////////////

  class ConventionalElectronicKeyboard (keyboard: Keyboard, powerAmp: PowerAmp, speaker: Speaker) {
    // functionality here
  }

  val conventionalElectronicKeyboard = new ConventionalElectronicKeyboard(
    new Keyboard () {override val keyCount = 49},
    new PowerAmp () {override val outputPower = 50},
    new Speaker () {override val impedance = 8; override val diameter = 10}
  )
}
