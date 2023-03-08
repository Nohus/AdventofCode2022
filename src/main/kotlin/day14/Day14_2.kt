package day14

import solve
import utils.Direction8.*
import utils.Point
import kotlin.math.sign

fun main() = solve { lines ->
    val map = mutableMapOf<Point, Char>()
    lines.forEach { line ->
        line.split(" -> ")
            .map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
            .windowed(2)
            .forEach { (start, end) ->
                val delta = Point((end.x - start.x).sign, (end.y - start.y).sign)
                var current = start - delta
                do {
                    current += delta
                    map[current] = '#'
                } while (current != end)
            }
    }
    val maxY = map.keys.maxOf { it.y } + 1

    outer@while (true) {
        var sand = Point(500, 0)
        while (true) {
            if (sand.y == maxY) break
            listOf(SOUTH, SOUTH_WEST, SOUTH_EAST).firstOrNull { !map.containsKey(sand.move(it)) }
                ?.let { sand = sand.move(it) }
                ?: if (sand == Point(500, 0)) break@outer else break
        }
        map[sand] = 'o'
    }

    map.values.count { it == 'o' } + 1
}
