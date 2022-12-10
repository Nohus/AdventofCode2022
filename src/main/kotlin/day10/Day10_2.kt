package day10

import solve
import utils.Point
import utils.printArea

fun main() = solve { lines ->
    var x = 1
    var cycle = 1
    val screen = mutableMapOf<Point, Boolean>()

    fun processCycle() {
        val screenX = (cycle - 1) % 40
        if (screenX in (x - 1)..(x + 1)) {
            screen[Point(screenX, (cycle - 1) / 40)] = true
        }
        cycle++
    }

    lines.forEach { line ->
        if (line == "noop") processCycle()
        else {
            processCycle(); processCycle()
            x += line.substringAfter(" ").toInt()
        }
    }
    screen.printArea()
}
