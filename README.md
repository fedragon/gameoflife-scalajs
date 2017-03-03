GameOfLifeFP
============
[A live demo here](https://amsterdam-scala.github.io/Sjs-HTML5Canvas-GameOfLive/public/view/index.html)

[The very core of the code can be found here](https://github.com/amsterdam-scala/Sjs-HTML5Canvas-GameOfLive/blob/master/src/main/scala-2.12/nl/amsscala/sjsgameoflive/LivingWorld.scala#L42-L76)

[Installation instructions here](#installation-instructions)

#### Conway's Game of Life - Functional Programmed in Scala.js

A scala implementation of Conway's Game of Life, a cellular automaton devised in 1970 by the British mathematician John Horton Conway. It is considered a zero-player or solitaire game because its evolution is determined by its initial state, requiring no further input from humans.

It is the best-known example of a cellular automaton and probably the most often programmed computer game in existence.

A Conway grid is infinite two-dimensional orthogonal. In fact, Conway's Game of Life is Turing complete, so this implementation avoids using fixed-size structures such as arrays. Instead, each generation is represented by a __set of the "alive" cells__. So the presence of a coordinate in a set will signal the fact of a living cell.

This solution is programmed with best practice and pragmatics of functional programming in Scala with its idiomatics.

## Installation instructions
1. Clone the Github project to a new directory. This is the project directory which become the working directory of current folder.
1. Naturally, at least a Java SE Runtime Environment (JRE) is installed on your platform and has a path to it enables execution.
1. (Optional) Test this by submitting a `java -version` command in a [Command Line Interface (CLI, terminal)](https://en.wikipedia.org/wiki/Command-line_interface). The output should look like this:
```
java version "1.8.0_102"
Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.102-b14, mixed mode)
```
1. Make sure sbt is runnable from almost any work directory, use eventually one of the platform depended installers:
    1. [Installing sbt on Mac](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Mac.html) or
    1. [Installing sbt on Windows](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Windows.html) or
    1. [Installing sbt on Linux](http://www.scala-sbt.org/release/docs/Installing-sbt-on-Linux.html) or
    1. [Manual installation](http://www.scala-sbt.org/release/docs/Manual-Installation.html) (not recommended)
1. (Optional ) To test if sbt is effective submit the `sbt sbtVersion` command. The response could look like as this:
```
[info] Set current project to fransdev (in build file:/C:/Users/FransDev/)
[info] 0.13.13
```
Remember shells (CLI's) are not reactive. To pick up the new [environment variables](https://en.wikipedia.org/wiki/Environment_variable) the CLI must be closed and restarted.

3. Run sbt in one of the next modes in a CLI in the working directory or current folder, a compilation will be started and a local web server will be spinned up using:
    1. Inline mode on the command line: `sbt fastOptJS` or
    1. Interactive mode, start first the sbt by hitting in the CLI `sbt` followed by `fastOptJS` on the sbt prompt, or
    1. Triggered execution by a `~` before the command, so `~fastOptJS`. This command will execute and wait after the target code is in time behind the source code (Auto build).
    1. `chrome:test` will run the ScalaTest test scripts in the test directory in a Google Chrome browser.
    1. `firefox:test` will start the ScalaTest test scripts in the test directory in a FireFox browser. (preferred)
1.  sbt will give a notice that the server is listening by the message: `Bound to localhost/127.0.0.1:12345`
    (Ignore the dead letter notifications with the enter key.)
1. Open this application in a browser on [this given URL](http://localhost:12345/target/scala-2.12/classes/index-dev.html)

When running this way a tool ["workbench"](https://github.com/lihaoyi/workbench) also will be running in the browser, noticeable by opening the console of the browser.