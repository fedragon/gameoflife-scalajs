package nl.amsscala
package sjsgameoflive

import org.scalajs.dom
import org.scalajs.dom.html.Canvas

import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.Dynamic
import scalatags.JsDom.all._


/** Everything related to Html5 visuals as put on a HTML page. */
trait Page {
  lazy val origin: Position[Int] = {
    val org = center(canvas)
    Position(org.x - org.x % cellSize, org.y - org.y % cellSize)
  }
  private lazy val runOnce = // Create the HTML body element with content
    dom.document.body.appendChild(div(cls := "content", style := "text-align:center; background-color:#3F8630;",
      canvas,
      a(href := "#",
        title := s"This object code is compiled with type parameter ${genericDetect(0D.asInstanceOf[HTML5CanvasGoL.T])}.",
        "HTML5 Canvas Game of Life"), " powered by ",
      a(href := "http://www.scala-js.org/", "Scala.js")).render)
  //Create canvas with a 2D processor
  protected val canvas: Canvas = dom.document.createElement("canvas").asInstanceOf[dom.html.Canvas]
  private val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
  private var cellSize = 10

  /**
    * Draw everything accordingly the given `GameState`.
    *
    * Order: Playground, Monster, Hero, monstersHitTxt, explainTxt/gameOverTxt
    *
    * @param gs Game state to make the graphics.
    * @return The same gs
    */
  def render(gs: LivingWorld, generation: Int): LivingWorld = {

    def drawCells(cells: LivingWorld) = {
      ctx.fillStyle = "white"
      cells.foreach(cell => {
        val pos = origin + cell * cellSize
        ctx.rect(pos.x, pos.y, cellSize, cellSize)
      })
    }

    def thousandSeparator(n: Int): Dynamic = n.asInstanceOf[js.Dynamic].toLocaleString("nl-NL", js.Dynamic.literal())

    runOnce

    ctx.beginPath()
    ctx.strokeStyle = "black"
    for (x <- 0 to origin.x * 2 by cellSize;
         y <- 0 to origin.y * 2 by cellSize) {
      ctx.moveTo(0, y)
      ctx.lineTo(ctx.canvas.width, y)
      ctx.moveTo(x, 0)
      ctx.lineTo(x, ctx.canvas.height)
    }

    val cellInWondow = LivingWorld.containedInRect(gs, LivingWorld.convertPx2CellCoord(canvasDim(canvas), origin))
    drawCells(cellInWondow)
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    ctx.fill()
    ctx.stroke()

    ctx.fillStyle = "orange"
    ctx.font = "24px Helvetica"
    ctx.textAlign = "left"
    ctx.textBaseline = "top"
    val format = asInstanceOf[js.Dynamic].toLocaleString("nl-NL", js.Dynamic.literal())
    val total = gs.size
    val visible = LivingWorld.containedInRect(gs, Position(52, 26)).size
    ctx.fillText(
      f"""Generation ${thousandSeparator(generation)}%8s. Population ${
        thousandSeparator(total)
      }%6s, visible: ${thousandSeparator(visible)}%5s""", 32, 32)

    gs
  }

  def canvasDim[D](cnvs: dom.html.Canvas): Position[D] = Position(cnvs.width, cnvs.height).asInstanceOf[Position[D]]

  def center(cnvs: dom.html.Canvas): Position[Int] = Position(canvas.width / 2, canvas.height / 2)

  /** Convert the onload event of an img tag into a Future */
  def imageFuture(src: String): Future[dom.raw.HTMLImageElement] = {
    val img = dom.document.createElement("img").asInstanceOf[dom.raw.HTMLImageElement]

    // Tackling CORS enabled images
    img.setAttribute("crossOrigin", "anonymous")
    img.src = src
    if (img.complete) Future.successful(img)
    else {
      val p = Promise[dom.raw.HTMLImageElement]()
      img.onload = { (e: dom.Event) => p.success(img) }
      p.future
    }
  }

  /**
    * Set canvas dimension
    *
    * @param cnvs Canvas element
    * @param pos  Dimension in `Position[P]`
    * @tparam P Numeric generic type
    */
  @inline
  private def resetCanvasWH[P: Numeric](cnvs: dom.html.Canvas, pos: Position[P]) = {
    cnvs.width = pos.x.asInstanceOf[Int]
    cnvs.height = pos.y.asInstanceOf[Int]
  }

  @inline private def dimension(img: dom.raw.HTMLImageElement) = Position(img.width, img.height)

  private def genericDetect(x: Any) = x match {
    case _: Long => "Long"
    case _: Int => "Int"
    case _: Double => "Double"
    case _ => "unknown"
  }

  canvas.textContent = "Your browser doesn't support the HTML5 <CANVAS> tag."
  resetCanvasWH(canvas, Position(dom.window.innerWidth, dom.window.innerHeight - 25))
}
