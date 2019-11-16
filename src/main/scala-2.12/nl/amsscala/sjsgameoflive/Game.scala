package nl.amsscala
package sjsgameoflive

import org.scalajs.dom

/** This game with its comprehensible rules. */
protected trait Game {
  this: Page =>

  /**
    * Initialize Game loop
    *
    * @param canvas   The visual html element
    * @param headless An option to run for testing
    */
  protected def play(canvas: dom.html.Canvas, headless: Boolean) {
    // Keyboard events store

    // Collect all Futures of onload events


    //var prevGS = new GameState(canvas, gameState.pageElements.zip(load).map { case (el, img) => el.copy(img = img) })

    /** The main game loop, by interval callback invoked. */


    def gameLoop() = {
      //val nowTimestamp = js.Date.now()
      // val actualGS = prevGS.keyEffect((nowTimestamp - prevTimestamp) / 1000, keysPressed)

      render(LivingWorld.tick(), LivingWorld.generation)

      // Render the <canvas> conditional only by movement of Hero, saves power
      // if (prevGS != actualGS) prevGS = render(actualGS)
    }

    render(LivingWorld.randomize(), 0) // First draw

    // Let's see how this game plays!
    if (!headless) {
      // For test purpose, a facility to silence the listeners.
      scala.scalajs.js.timers.setInterval(300)(gameLoop())

      // TODO: mobile application navigation

      canvas.onclick =
        (e: dom.MouseEvent) =>
          LivingWorld.add(Position(e.clientX.toInt, e.clientY.toInt), origin)


    }
  }

}
