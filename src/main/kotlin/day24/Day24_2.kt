package day24

import solve
import utils.Direction
import utils.Point

fun main() = solve { lines ->
    val walls = mutableSetOf<Point>()
    val blizzards = mutableSetOf<Pair<Point, Direction>>()
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            when (char) {
                '#' -> walls += Point(x, y)
                '>' -> blizzards += Point(x, y) to Direction.EAST
                '<' -> blizzards += Point(x, y) to Direction.WEST
                '^' -> blizzards += Point(x, y) to Direction.NORTH
                'v' -> blizzards += Point(x, y) to Direction.SOUTH
            }
        }
    }
    val start = Point(1, 0)
    val end = Point(walls.maxOf { it.x } - 1, walls.maxOf { it.y })
    val width = walls.maxOf { it.x } - 1
    val height = walls.maxOf { it.y } - 1

    val blizzardsCache = mutableMapOf<Int, Set<Point>>()
    fun blizzardsAt(minute: Int): Set<Point> {
        blizzardsCache[minute]?.let { return it }
        return blizzards.map { (point, direction) ->
            var new = point.move(direction, minute)
            when (direction) {
                Direction.NORTH -> while (new.y < 1) new = new.copy(y = new.y + height)
                Direction.EAST -> while (new.x > width) new = new.copy(x = new.x - width)
                Direction.SOUTH -> while (new.y > height) new = new.copy(y = new.y - height)
                Direction.WEST -> while (new.x < 1) new = new.copy(x = new.x + width)
            }
            new
        }.toSet().also { blizzardsCache[minute] = it }
    }

    data class Node(val point: Point, val minute: Int)

    fun search(from: Point, to: Point, startingMinute: Int): Int {
        val unvisited = mutableListOf<Node>()
        unvisited += Node(from, startingMinute)
        while (true) {
            val current = unvisited.removeFirst()
            if (current.point == to) return current.minute
            val blizzardsNext = blizzardsAt(current.minute + 1)
            val possible = (current.point.getAdjacentSides()
                .filter { it !in walls }
                .filter { it.x >= start.x && it.x <= end.x && it.y >= start.y && it.y <= end.y }
                .map { Node(it, current.minute + 1) } + Node(current.point, current.minute + 1))
                .filter { it.point !in blizzardsNext }
            unvisited += possible.filter { p -> p.minute < (unvisited.filter { it.point == p.point }.minOfOrNull { it.minute } ?: Int.MAX_VALUE) }
        }
    }

    search(start, end, search(end, start, search(start, end, 0)))
}
