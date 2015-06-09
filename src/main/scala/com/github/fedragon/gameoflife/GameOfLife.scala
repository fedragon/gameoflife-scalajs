package com.github.fedragon.gameoflife

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

@JSExport
object GameOfLife {
  val BoardSize = 10
  val CellSize = 30

  case class Cell(x: Int, y: Int, alive: Boolean) {
    def survives(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      alive && (aliveNeighbours.size == 2 || aliveNeighbours.size == 3)
    }

    def dies(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      alive && (aliveNeighbours.size < 2 || aliveNeighbours.size > 3)
    }

    def reproduces(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      !alive && aliveNeighbours.size == 3
    }

    def evolve(neighbours: Seq[Cell]) =
      if(reproduces(neighbours)) copy(alive = true)
      else if(survives(neighbours)) this
      else copy(alive = false)

      def move(dx: Int, dy: Int) = (x + dx, y + dy)

      def possibleNeighbours = {
        move(-1, -1) :: move(-1, 0) :: move(-1, 1) ::
        move(0, -1) :: move(0, 1) ::
        move(1, -1) :: move(1, 0) :: move(1, 1) ::
        Nil
      }
  }

  def exists(xy: (Int, Int)) = {
    val (x, y) = xy
    x >= 0 && x < BoardSize && y >=0 && y < BoardSize
  }

  def neighboursOf(cell: Cell, cells: Seq[Cell]) = {
    val indexes = cell.possibleNeighbours.filter(exists)
    cells.filter {
      case Cell(x, y, _) => indexes.contains((x, y))
    }
  }

  def tick(cells: Seq[Cell]) =
    cells.zipWithIndex.map {
      case (cell, index) =>
        val neighbours = neighboursOf(cell, cells)
        cell.evolve(neighbours)
    }

  def generateBoard: Seq[Cell] = {
    def randomCells(i: Int, acc: Seq[(Int, Int)]): Seq[(Int, Int)] =
      if(i == 0) acc
      else randomCells(i - 1, (Random.nextInt(BoardSize), Random.nextInt(BoardSize)) +: acc)

    val aliveCells = randomCells(BoardSize + Random.nextInt(BoardSize * 2), Nil)
    println(s"generated alive cells: $aliveCells")

    for {
      i <- 0 until BoardSize
      j <- 0 until BoardSize
    } yield {
      Cell(i, j, aliveCells.contains((i, j)))
    }
  }

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = BoardSize * CellSize
    canvas.height = BoardSize * CellSize

    var seed = generateBoard

    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, CellSize * BoardSize, CellSize * BoardSize)

    def run = {
      seed.zipWithIndex.map { case (cell, index) =>
        val x = cell.x * CellSize
        val y = cell.y * CellSize
        val style = if (cell.alive) "blue" else "white"

        ctx.fillStyle = style
        ctx.fillRect(x, y, CellSize, CellSize)
      }

      seed = tick(seed)
    }

    dom.setInterval(() => run, 500)
  }
}
