package day22

import solve
import utils.Direction.*
import utils.Point
import utils.Turn

fun main() = solve(trim = false) { lines ->
    val map = mutableMapOf<Point, Char>()
    lines.dropLast(2).forEachIndexed { y, row ->
        row.forEachIndexed { x, char ->
            if (char != ' ') map[Point(x, y)] = char
        }
    }
    var position = map.minBy { it.key.y * 1000 + it.key.x }.key
    var facing = EAST
    var unconsumed = lines.last()
    while (unconsumed.isNotEmpty()) {
        val move = unconsumed.takeWhile { it.isDigit() }
        unconsumed = unconsumed.removePrefix(move)
        for (step in 0 until move.toInt()) {
            var tile = position.move(facing)
            if (!map.containsKey(tile)) {
                var backtracking = position
                while (true) {
                    tile = backtracking
                    backtracking = backtracking.move(facing.reverse())
                    if (!map.containsKey(backtracking)) break
                }
            }
            if (map[tile] == '.') {
                position = tile
            } else if (map[tile] == '#') {
                break
            }
        }
        if (unconsumed.isNotEmpty()) {
            val letter = unconsumed.take(1)
            unconsumed = unconsumed.drop(1)
            val turn = when (letter) {
                "L" -> Turn.LEFT
                "R" -> Turn.RIGHT
                else -> throw RuntimeException(letter)
            }
            facing = facing.rotate(turn)
        }
    }
    val facingValue = when (facing) {
        NORTH -> 3
        EAST -> 0
        SOUTH -> 1
        WEST -> 2
    }
    (position.y + 1) * 1000 + (position.x + 1) * 4 + facingValue
}
