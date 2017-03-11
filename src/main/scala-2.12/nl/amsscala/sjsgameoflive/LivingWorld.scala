package nl.amsscala
package sjsgameoflive

object LivingWorld {
  lazy val r = scala.util.Random
  var generation = 0
  var livingWorld: LivingWorld = Set.empty

  def add(clickPos: Position[Int], origin: Position[Int]): Unit = {
    livingWorld += convertPx2CellCoord(clickPos, origin)
  }

  def apply(offset: Position[Int], pos: (Int, Int)*): LivingWorld = pos.map { case (x, y) => Position(x, y) + offset }.toSet

  def apply(offset: Position[Int], set: LivingWorld): LivingWorld = set.map { case pos => pos + offset }.toSet

  def apply(pos: (Int, Int)*): LivingWorld = pos.map { case (x, y) => Position(x, y) }.toSet

  def convertPx2CellCoord(clickPos: Position[Int], origin: Position[Int]): Position[Int] = {
    val centered = clickPos - (origin + 5)
    Position((centered.x / 10.0).round.toInt, (centered.y / 10.0).round.toInt)
  }

  def containedInRect(gs: LivingWorld, rect: Position[Int]): LivingWorld = gs.filter(_.hasOverlap(Position(0, 0), rect))

  def randomize(): LivingWorld = {
    livingWorld = (for (n <- 0 to 4000) yield Position(r.nextInt(160) - 80, r.nextInt(80) - 40)).toSet
    livingWorld
  }

  /**
    * Function to transform a "world" applying the GoL rules to a next "world"
    *
    * @param rulestringB number(s) of neighbors to give birth
    * @param rulestringS numbers of neighbors to survive.
    * @return the new world
    */
  def tick(world: LivingWorld = livingWorld,
           rulestringB: Set[Int] = Set(3), // Default to Conway's GoL B3S23
           rulestringS: Set[Int] = Set(2, 3)): LivingWorld = {
    assume(generation != Long.MaxValue, "Generations outnumbered")
    generation += 1

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     *    H E R E   T H E   M A G I C   S T A R T S    *
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** A Map containing only ''coordinates'' that are neighbors of alive cells
      * which alive, together with the ''number'' of XYpos it is neighbor of.
      * eg. Position(42,24) -> 3
      */
    val neighbors: Map[Position[Int], Int] =
      world.toSeq.flatMap(_.mooreNeighborHood(1)).groupBy(identity).map { case (pos, list) => (pos, list.size) }

    /* A. Filter all neighbors for desired characteristics */

    // Criterion of rule string Birth applied on dead cells gives the fresh reproduces.
    // Note: A survivor criterion for the alive cell is common. These are included too!
    val reproduces: Set[Position[Int]] = neighbors.filter { case (_, n) => rulestringB contains n }.keySet

    /* B. Apply the criterion of Survivors rule string */
    // First, exclude common Birth and Survivor criteria, these are already filtered, processing this would be useless.
    val surviveNumbers = rulestringS -- rulestringB

    /**
      * Secondly, this function gives you all survivors.
      * @return all survivors
      */
    @inline
    def survivors(): Set[Position[Int]] = {
      // Meet the criteria about number of neighbors AND they must be alive.
      (neighbors -- reproduces).filter { case (_, n) => surviveNumbers contains n }.keySet intersect world
    }

    // Putting it all together
    livingWorld = survivors() ++ reproduces
    livingWorld
  } // def tick(…

}
