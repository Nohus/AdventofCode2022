package day12

import solve
import utils.Point

fun main() = solve { lines ->
    val map = mutableMapOf<Point, Char>()
    val shortest = mutableMapOf<Point, Int>()
    var end = Point.ORIGIN
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char == 'E') end = Point(x, y)
            map[Point(x, y)] = if (char == 'S') 'a' else if (char == 'E') 'z' else char
        }
    }

    fun bfs(position: Point, visited: List<Point>, steps: Int): Int? {
        if (position == end) return steps
        shortest[position] = steps
        return position.getAdjacentSides().asSequence()
            .filter { map.containsKey(it) && it !in visited }
            .filter { map[it]!! <= map[position]!! + 1 }
            .filter { steps + 1 < shortest.getOrDefault(it, Int.MAX_VALUE) }
            .mapNotNull { bfs(it, visited + position, steps + 1) }
            .minOrNull()
    }

    map
        .filter { it.value == 'a' }
        .map { it.key }
        .sortedBy { it.distance(end) }
        .mapNotNull { bfs(it, emptyList(), 0) }
        .min()
}
