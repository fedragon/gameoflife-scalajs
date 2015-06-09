package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

@JSExport
object GameOfLife {
  val Alive = true
  val Dead = false

  case class Cell(x: Int, y: Int, alive: Boolean) {
    def willSurvive(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      alive && (aliveNeighbours.size == 2 || aliveNeighbours.size == 3)
    }

    def willDie(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      alive && (aliveNeighbours.size < 2 || aliveNeighbours.size > 3)
    }

    def willReproduce(neighbours: Seq[Cell]) = {
      val aliveNeighbours = neighbours.filter(_.alive)
      !alive && aliveNeighbours.size == 3
    }

    def evolve(neighbours: Seq[Cell]) =
      if(willReproduce(neighbours)) copy(alive = Alive)
      else if(willSurvive(neighbours)) this
      else copy(alive = Dead)

      def move(dx: Int, dy: Int) = (x + dx, y + dy)

      def square = {
        move(-1, -1) :: move(-1, 0) :: move(-1, 1) ::
        move(0, -1) :: move(0, 1) ::
        move(1, -1) :: move(1, 0) :: move(1, 1) ::
        Nil
      }
  }

  def isValidMove(xy: (Int, Int)) = {
    val (x, y) = xy
    x >= 0 && x < 3 && y >=0 && y < 3
  }

  def neighboursOf(cell: Cell, cells: Seq[Cell]) = {
    val indexes = cell.square.filter(isValidMove)
    cells.filter {
      case Cell(x, y, _) => indexes.contains((x, y))
    }
  }

  def nextGeneration(cells: Seq[Cell]) =
    cells.zipWithIndex.map {
      case (cell, index) =>
        val neighbours = neighboursOf(cell, cells)
        val alien = cell.evolve(neighbours)
        println(s"cell: $cell, neighbours: $neighbours => $alien")
        alien
    }

  //def overcrowded = {
    //val cells = Seq(Cell(Alive), Cell(Alive), Cell(Alive),
                    //Cell(Alive), Cell(Alive), Cell(Alive),
                    //Cell(Alive), Cell(Alive), Cell(Alive))

    //println("overcrowded: " + nextGeneration(cells))
  //}

  //def underpopulated = {
    //val cells = Seq(Cell(Dead),  Cell(Dead),  Cell(Dead),
                    //Cell(Alive), Cell(Alive), Cell(Dead),
                    //Cell(Dead),  Cell(Dead),  Cell(Dead))

    //println("underpopulated: " + nextGeneration(cells))
  //}

  //def survive = {
    //val cells = Seq(Cell(Dead),  Cell(Dead),  Cell(Dead),
                    //Cell(Alive), Cell(Alive), Cell(Alive),
                    //Cell(Dead),  Cell(Dead),  Cell(Dead))

    //println("survive: " + nextGeneration(cells))
  //}

  //def reproduce = {
    //val cells = Seq(Cell(Dead),  Cell(Alive),  Cell(Dead),
                    //Cell(Alive), Cell(Alive), Cell(Alive),
                    //Cell(Dead),  Cell(Alive),  Cell(Dead))

    //println("reproduce: " + nextGeneration(cells))
  //}

  //@JSExport
  //def main(): Unit = {
    //overcrowded
    //underpopulated
    //survive
    //reproduce
  //}

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d")
                    .asInstanceOf[dom.CanvasRenderingContext2D]

    var cells = Seq(Cell(0, 0, Dead),  Cell(0, 1, Alive), Cell(0, 2, Dead),
                    Cell(1, 0, Alive), Cell(1, 1, Alive), Cell(1, 2, Dead),
                    Cell(2, 0, Dead),  Cell(2, 1, Dead),  Cell(2, 2, Alive))

    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, 300, 300)

    def run = {
      val size = 85
      cells.zipWithIndex.map { case (cell, index) =>
        val y = cell.y * size
        val x = cell.x * size
        val style = if (cell.alive) "green" else "red"
        println(s"$index => ($x, $y) => $style")

        //ctx.fillStyle = "black"
        //ctx.rect(x, y, size, size)
        //ctx.stroke

        ctx.fillStyle = style
        ctx.fillRect(x, y, size, size)
      }

      cells = nextGeneration(cells)
    }

    dom.setInterval(() => run, 1000)
  }
}
