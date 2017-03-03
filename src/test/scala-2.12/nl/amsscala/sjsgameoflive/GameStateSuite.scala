package nl.amsscala
package sjsgameoflive

class GameStateSuite extends SuiteSpec {
  val ran = scala.util.Random
  val offsetPosition = Position(ran.nextInt(), ran.nextInt())
  describe("Living world") {
    describe("should perform world to next world function for blinkers") {
      it("should transform 3 alive cell vertically") {
        assert(LivingWorld.tick(LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1))) ===
          LivingWorld(offsetPosition, (0, 0), (-1, 0), (1, 0)))
      }
      it("should transform 3 alive cell horizontally") {
        assert(LivingWorld.tick(LivingWorld(offsetPosition, (0, 0), (-1, 0), (1, 0))) ===
          LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1)))
      }
      val original6v = LivingWorld(offsetPosition, (0, 0), (0, 1), (0, -1), (1, 0), (1, -1), (1, -2))
      val original6h = original6v.map { case Position(x, y) => Position(y, x) } // Transpose
      it("should transform 6 alive cell vertically") {
        assert(LivingWorld.tick(original6v) === LivingWorld(offsetPosition, (2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1)))
      }
      it("should transform blinker6 back") {
        assert(LivingWorld.tick(LivingWorld(offsetPosition, (2, -1), (1, -2), (1, 1), (0, -2), (-1, 0), (0, 1))) === original6v)
      }
      it("should transform blinker6 horizontally twice") {
        assert(LivingWorld.tick(LivingWorld.tick(original6h)) === original6h)
      }
    }
  }
}
