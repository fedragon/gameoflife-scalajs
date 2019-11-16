package nl.amsscala
package sjsgameoflive

import org.scalatest.{FeatureSpec, GivenWhenThen, Matchers}

class GoLGivenWhenThenSpec extends FeatureSpec with GivenWhenThen with Matchers {

  info("As a GoL tester")
  info("I want to test the tick function")
  info("The tick function populates the GoL world with a new generation")

  val ran = scala.util.Random
  val offsetPosition = Position(ran.nextInt(), ran.nextInt())

  feature("Test two-periods Oscillators") {
    scenario("test a Blinker Oscillator (period 2)") {

      Given("3 alive cell vertically")
      val threeCellVertically = LivingWorld((0, 0), (0, 1), (0, -1))
      val orgLivingWorld = LivingWorld(offsetPosition, threeCellVertically)

      orgLivingWorld shouldBe LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1))

      When("the tick function transfers the world to a next state")
      val nextLivingWorld = LivingWorld.tick(orgLivingWorld)

      Then("the 3 living cell should be arranged horizontally")
      nextLivingWorld shouldBe LivingWorld(offsetPosition,
        threeCellVertically.map { case Position(x, y) => Position(y, x) } /*Transpose*/)

      And("finally put back to the original vertical state")
      LivingWorld.tick(nextLivingWorld) shouldBe orgLivingWorld

    }

    scenario("test a Toad Oscillator (period 2)") {

      Given("6 alive cells vertically")
      val sixCellsVertically = LivingWorld((0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))
      val original6v = LivingWorld(offsetPosition, sixCellsVertically)
      original6v shouldBe LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))

      When("the tick function transfers the world to a next state")
      val nextLivingWorld = LivingWorld.tick(original6v)

      Then("the group of cells has another morphology")
      nextLivingWorld shouldBe LivingWorld(offsetPosition, (2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1))

      And("but finally should transforms back to its orginal shape")
      LivingWorld.tick(nextLivingWorld) shouldBe original6v


      Given("6 alive cells horizontally")
      val sixCellsHorizontally = sixCellsVertically.map { case Position(x, y) => Position(y, x) }
      /*Transpose*/
      val original6h = LivingWorld(offsetPosition, sixCellsHorizontally)
      original6h shouldBe LivingWorld(offsetPosition, sixCellsVertically.map { case Position(x, y) => Position(y, x) } /*Transpose*/)

      When("the tick function transfers the world to a next state")
      val nextLivingWorldH = LivingWorld.tick(original6h)

      Then("the group of cells has another morphology")
      nextLivingWorldH shouldBe LivingWorld(offsetPosition, (-2, 0), (-2, 1), (1, 1), (-1, 2), (1, 0), (0, -1))

      And("but finally should transforms back to its orginal shape")
      LivingWorld.tick(nextLivingWorldH) shouldBe original6h
    }
  }
}

