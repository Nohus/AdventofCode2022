package day09

import solve
import utils.Direction
import utils.Direction.*
import utils.Point
import kotlin.math.sign

fun main() = solve { lines ->
    val knots = MutableList(10) { Point.ORIGIN }
    val visited = mutableSetOf(Point.ORIGIN)
    lines.forEach { line ->
        val direction: Direction = when (line.substringBefore(" ")) {
            "U" -> NORTH
            "L" -> WEST
            "R" -> EAST
            "D" -> SOUTH
            else -> error("")
        }
        repeat(line.substringAfter(" ").toInt()) {
            knots[0] = knots[0].move(direction)
            knots.drop(1).indices.forEach { index ->
                val head = knots[index]
                var tail = knots[index + 1]
                if (tail !in head.getAdjacent()) {
                    tail = Point(tail.x + (head.x - tail.x).sign, tail.y + (head.y - tail.y).sign)
                    visited += knots.last()
                }
                knots[index + 1] = tail
            }
        }
    }
    visited.size
}
