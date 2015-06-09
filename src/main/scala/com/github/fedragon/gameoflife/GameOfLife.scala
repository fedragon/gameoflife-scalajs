package com.github.fedragon.gameoflife

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

@JSExport
object GameOfLife {
  val BoardSize = 30
  val CellSize = 30

  case class Cell(x: Int, y: Int, alive: Boolean) {
    def survives(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.count(_.alive)
      alive && (aliveNeighbours == 2 || aliveNeighbours == 3)
    }

    def dies(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.count(_.alive)
      alive && (aliveNeighbours < 2 || aliveNeighbours > 3)
    }

    def reproduces(neighbours: Seq[Cell]) = !alive && neighbours.count(_.alive) == 3

    def evolve(neighbours: Seq[Cell]) =
      if(reproduces(neighbours)) copy(alive = true)
      else if(survives(neighbours)) this
      else copy(alive = false)
  }

  def neighboursOf(cell: Cell, cells: Seq[Cell]) = {
    cells.filter {
      case Cell(x, y, _) =>
        Math.abs(cell.x - x) <= 1 && Math.abs(cell.y - y) <= 1
    }
  }

  def tick(cells: Seq[Cell]) =
    cells.map { cell =>
      val neighbours = neighboursOf(cell, cells)
      cell.evolve(neighbours)
    }

  def generateBoard: Seq[Cell] = {
    val aliveCells = List.fill(BoardSize + Random.nextInt(BoardSize * 2))((Random.nextInt(BoardSize), Random.nextInt(BoardSize)))

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
