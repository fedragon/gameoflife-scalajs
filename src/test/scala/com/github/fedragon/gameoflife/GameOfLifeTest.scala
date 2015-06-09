package com.github.fedragon.gameoflife

import utest._

object GameOfLifeTest extends TestSuite {

  import GameOfLife._

  val tests = TestSuite {
    "A live cell"-{
      "should survive with 2 or 3 live neighbours"-{
        val subject = Cell(0, 0, true)
        val neighbours = Seq(Cell(0, 1, true), Cell(1, 0, true))

        assert(subject.evolve(neighbours).alive == true)
        assert(subject.evolve(neighbours :+ Cell(1, 1, true)).alive == true)
      }
      "should die by under population"-{
        val subject = Cell(1, 1, true)
        val neighbours = Seq(Cell(0, 1, true), Cell(1, 0, false), Cell(1, 2, false))

        assert(subject.evolve(neighbours).alive == false)
      }
      "should die by overcrowding"-{
        val subject = Cell(1, 1, true)
        val neighbours = Seq(Cell(0, 1, true), Cell(1, 0, true), Cell(2, 1, true), Cell(1, 2, true))

        assert(subject.evolve(neighbours).alive == false)
      }
    }
    "A dead cell"-{
      "should reproduce with exactly 3 live neighbours"-{
        val subject = Cell(1, 1, false)
        val neighbours = Seq(Cell(0, 1, true), Cell(1, 0, true), Cell(2, 1, true))

        assert(subject.evolve(neighbours).alive == true)
      }
    }
  }
}
