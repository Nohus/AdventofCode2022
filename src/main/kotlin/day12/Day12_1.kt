package day12

import solve
import utils.Point

fun main() = solve { lines ->
    val map = mutableMapOf<Point, Char>()
    var start = Point.ORIGIN
    var end = Point.ORIGIN
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == 'S') {
                start = Point(x, y)
                map[start] = 'a'
            } else if (char == 'E') {
                end = Point(x, y)
                map[end] = 'z'
            } else {
                map[Point(x, y)] = char
            }
        }
    }
    val finishes = mutableListOf<Int>()
    val shortest = mutableMapOf<Point, Int>()
    fun bfs(position: Point, visited: List<Point>, steps: Int) {
        if (position == end) {
            println("Reached end in $steps")
            finishes += steps
            return
        }
        shortest[position] = steps
        val elevation = map[position]!!
        val possible = position.getAdjacentSides()
            .filter { it !in visited }
            .filter { map.containsKey(it) }
            .filter { map.getValue(it) <= elevation + 1 }
            .filter { steps + 1 < shortest.getOrDefault(it, Int.MAX_VALUE) }
            .sortedBy { it.distance(end) }
        possible.forEach {
            bfs(it, visited + position, steps + 1)
        }
    }
    bfs(start, emptyList(), 0)
    finishes.min()
}
