package day23

import solve
import utils.Direction8.*
import utils.Point

fun main() = solve { lines ->
    val elves = mutableSetOf<Point>()
    lines.forEachIndexed { y, row -> row.forEachIndexed { x, char -> if (char == '#') elves += Point(x, y) } }
    val directions = listOf(
        listOf(NORTH, NORTH_EAST, NORTH_WEST), listOf(SOUTH, SOUTH_EAST, SOUTH_WEST),
        listOf(WEST, NORTH_WEST, SOUTH_WEST), listOf(EAST, NORTH_EAST, SOUTH_EAST)
    )
    var round = 1
    while (true) {
        val shift = (round - 1) % 4
        val moves = elves.mapNotNull { position ->
            if (position.getAdjacent().any { it in elves }) {
                (directions.drop(shift) + directions.take(shift))
                    .firstOrNull { it.none { position.move(it) in elves } }
                    ?.let { position to position.move(it.first()) }
            } else null
        }.toMap()
        val valid = moves.values.groupBy { it }.filter { it.value.size == 1 }.map { it.key }
        moves.filter { it.value in valid }.forEach { (from, to) -> elves -= from; elves += to }
        if (moves.isEmpty()) break
        round++
    }
    round
}
