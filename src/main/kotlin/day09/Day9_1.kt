package day09

import solve
import utils.Direction
import utils.Direction.*
import utils.Point
import kotlin.math.sign

fun main() = solve { lines ->
    var head = Point.ORIGIN
    var tail = Point.ORIGIN
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
            head = head.move(direction)
            if (tail !in head.getAdjacent()) {
                tail = Point(tail.x + (head.x - tail.x).sign, tail.y + (head.y - tail.y).sign)
                visited += tail
            }
        }
    }
    visited.size
}
