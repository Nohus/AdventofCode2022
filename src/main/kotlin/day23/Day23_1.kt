package day23

import solve
import utils.Direction8
import utils.Direction8.*
import utils.Point
import utils.printArea

fun main() = solve { lines ->
    val map = mutableMapOf<Point, Char>()
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            map[Point(x, y)] = char
        }
    }
    map.printArea()

    var shift = 0
    repeat(10) { round ->
        var moves = mutableMapOf<Point, Direction8>()
        map.filter { it.value == '#' }.forEach { (position, elf) ->
            if (position.getAdjacent().map { map[it] }.none { it == '#' }) {
                // do nothing
            } else {
                val a = listOf(
                    listOf(NORTH, NORTH_EAST, NORTH_WEST),
                    listOf(SOUTH, SOUTH_EAST, SOUTH_WEST),
                    listOf(WEST, NORTH_WEST, SOUTH_WEST),
                    listOf(EAST, NORTH_EAST, SOUTH_EAST)
                )
                (a.drop(shift) + a.take(shift)).firstOrNull {
                    it.map { map[position.move(it)] }.none { it == '#' }
                }?.let {
                    moves[position] = it.first()
                }
            }
        }
        val moves2 = moves.map { it.key to it.key.move(it.value) }.toMap()
        val validTargets = moves2.values.groupBy { it }.map { it.key to it.value.size }.filter { it.second == 1 }.map { it.first }
        moves2.filter { it.value in validTargets }.forEach { (from, to) ->
            map[from] = '.'
            map[to] = '#'
        }
        map.printArea()
        shift++
        if (shift >= 4) shift = 0
    }
    val elves = map.filter { it.value == '#' }.map { it.key }
    val minX = elves.minOf { it.x }
    val maxX = elves.maxOf { it.x }
    val minY = elves.minOf { it.y }
    val maxY = elves.maxOf { it.y }
    val width = (maxX - minX + 1)
    val height = (maxY - minY + 1)
    width * height - elves.size
}
