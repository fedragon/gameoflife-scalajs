package com.github.fedragon.gameoflife

import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js.annotation.JSExport
import scala.util.Random

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

@JSExport
object GameOfLife {
  val BoardSize = 15

  def neighboursOf(cell: Cell, cells: Seq[Cell]) =
    cells.filter {
      case other @ Cell(x, y, _) =>
        other != cell && Math.abs(cell.x - x) <= 1 && Math.abs(cell.y - y) <= 1
    }

  def tick(cells: Seq[Cell]) =
    cells.map { cell =>
      val neighbours = neighboursOf(cell, cells)
      cell.evolve(neighbours)
    }

  def generateBoard: Seq[Cell] = {
    val n = BoardSize * 2 + Random.nextInt(BoardSize * 2)
    val alive = Seq.fill(n)(
      (Random.nextInt(BoardSize), Random.nextInt(BoardSize))
    ).distinct

    println(s"generated ${alive.size} alive cells: $alive")

    for {
      i <- 0 until BoardSize
      j <- 0 until BoardSize
    } yield {
      Cell(i, j, alive.contains((i, j)))
    }
  }

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val cellSize = 30

    canvas.width = BoardSize * cellSize
    canvas.height = BoardSize * cellSize

    var seed = generateBoard

    ctx.fillStyle = "white"
    ctx.fillRect(0, 0, cellSize * BoardSize, cellSize * BoardSize)

    def run = {
      seed.zipWithIndex.map { case (cell, index) =>
        val x = cell.x * cellSize
        val y = cell.y * cellSize
        val style = if (cell.alive) "blue" else "white"

        ctx.fillStyle = style
        ctx.fillRect(x, y, cellSize, cellSize)
      }

      seed = tick(seed)
    }

    dom.setInterval(() => run, 500)
  }
}
