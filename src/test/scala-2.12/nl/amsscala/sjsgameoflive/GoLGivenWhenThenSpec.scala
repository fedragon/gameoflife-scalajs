package nl.amsscala
package sjsgameoflive

import org.scalatest.{FeatureSpec, GivenWhenThen}

class GoLGivenWhenThenSpec extends FeatureSpec with GivenWhenThen {

  info("As a GoL tester")
  info("I want to be test the tick function")
  info("The tick function populates the GoL world with a new generation")

  val ran = scala.util.Random
  val offsetPosition = Position(ran.nextInt(), ran.nextInt())

  feature("Test blinker") {
    scenario("test a Blinker Oscillator (period 2)") {

      Given("3 alive cell vertically")
      val threeCellVertically = LivingWorld((0, 0), (0, 1), (0, -1))
      val orgLivingWorld = LivingWorld(offsetPosition, threeCellVertically)

      assert(orgLivingWorld === LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1)))

      When("the tick function transfers the world to a next state")
      val nextLivingWorld = LivingWorld.tick(orgLivingWorld)

      Then("the 3 living cell should be arranged horizontally")
      assert(nextLivingWorld === LivingWorld(offsetPosition,
        threeCellVertically.map { case Position(x, y) => Position(y, x) } /*Transpose*/))

      And("put back to the original vertical state")
      assert(LivingWorld.tick(nextLivingWorld) === orgLivingWorld)

    }

    scenario("test a Toad Oscillator (period 2)") {

      Given("6 alive cells vertically")
      val sixCellsVertically = LivingWorld((0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))
      val original6v = LivingWorld(offsetPosition, sixCellsVertically)
      assert(original6v === LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2)))

      When("the tick function transfers the world to a next state")
      val nextLivingWorld = LivingWorld.tick(original6v)

      Then("the group of cells has another morphology")
      assert(nextLivingWorld === LivingWorld(offsetPosition, (2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1)))

      And("but should transforms back to its orginal shape")
      assert(LivingWorld.tick(nextLivingWorld) === original6v)


      Given("6 alive cells horizontally")
      val sixCellsHorizontally = sixCellsVertically.map { case Position(x, y) => Position(y, x) }
      /*Transpose*/
      val original6h = LivingWorld(offsetPosition, sixCellsHorizontally)
      assert(original6h === LivingWorld(offsetPosition, sixCellsVertically.map { case Position(x, y) => Position(y, x) } /*Transpose*/))

      When("the tick function transfers the world to a next state")
      val nextLivingWorldH = LivingWorld.tick(original6h)

      Then("the group of cells has another morphology")
      assert(nextLivingWorldH === LivingWorld(offsetPosition, (-2, 0), (-2, 1), (1, 1), (-1, 2), (1, 0), (0, -1)))

      And("but should transforms back to its orginal shape")
      assert(LivingWorld.tick(nextLivingWorldH) === original6h)
    }
  }
}


/*class GameStateSuite extends SuiteSpec {
  val ran = scala.util.Random
  val offsetPosition = Position(ran.nextInt(), ran.nextInt())
  describe("Living world") {
      it("should transform 3 alive cell horizontally") {
        assert(LivingWorld.tick(LivingWorld(offsetPosition, (0, 0), (-1, 0), (1, 0))) ===
          LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1)))
      }
      val original6v = LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))
      val original6h = original6v.map { case Position(x, y) => Position(y, x) } // Transpose
      it("should transform blinker6 back") {
        assert(LivingWorld.tick(LivingWorld(offsetPosition, (2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1))) === original6v)
      }
      it("should transform blinker6 horizontally twice") {
        assert(LivingWorld.tick(LivingWorld.tick(original6h)) === original6h)
      }
    }
  }*/

