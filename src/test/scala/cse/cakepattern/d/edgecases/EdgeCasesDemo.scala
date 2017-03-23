package cse.cakepattern.d.edgecases

import org.scalatest.path

/**
  * Created by dnwiebe on 3/19/17.
  */

object EdgeCasesDemo {
  trait OneWay {
    def method (): String = "One Way"
  }

  trait AnotherWay {
    def method (): String = "Another Way"
  }

  trait BothWays {
    this: OneWay with AnotherWay =>
  }

  /////////////////////////////////////////////

  trait DiamondTop {
    val string: String
    def method (): String = string
  }

  trait DiamondOneSide extends DiamondTop {
    override val string = "One Side"
  }

  trait DiamondAnotherSide extends DiamondTop {
    override val string = "Another Side"
  }

  trait DiamondBottom {
    this: DiamondOneSide with DiamondAnotherSide =>
  }
}

class EdgeCasesDemo extends path.FunSpec {
  import cse.cakepattern.d.edgecases.EdgeCasesDemo._

  describe ("Some edge cases include") {

    describe ("A container declared with two layers that implement methods with identical signatures") {

      it ("does not compile without a little help") {
//        val bothWays = new BothWays () with OneWay with AnotherWay
//        println (bothWays.method ())
      }

      it ("is fine when told which way to go") {
        val bothWays = new BothWays () with OneWay with AnotherWay {
          override def method (): String = super[OneWay].method ()
        }
        assert (bothWays.method () === "One Way")
      }
    }

    describe ("A classic C++-style Diamond of Death") {

      it ("uses the rightmost side of the diamond") {
        val bottom = new DiamondBottom () with DiamondOneSide with DiamondAnotherSide
        assert (bottom.method () === "Another Side")
      }

      it ("still uses the rightmost side of the diamond") {
        val bottom = new DiamondBottom () with DiamondAnotherSide with DiamondOneSide
        assert (bottom.method () === "One Side")
      }
    }
  }
}
