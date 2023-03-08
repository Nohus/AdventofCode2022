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
            var newFacing = facing
            if (!map.containsKey(tile)) {
                val topOffset = Point(50, 0)
                val rightOffset = Point(100, 0)
                val frontOffset = Point(50, 50)
                val leftOffset = Point(0, 100)
                val bottomOffset = Point(50, 100)
                val backOffset = Point(0, 150)
                when {
                    position.x in 50..99 && position.y in 0..49 -> {
                        // Top
                        val local = position - topOffset
                        when (facing) {
                            NORTH -> {
                                newFacing = EAST
                                tile = backOffset + Point(0, local.x)
                            }
                            EAST -> TODO()
                            SOUTH -> TODO()
                            WEST -> {
                                newFacing = EAST
                                tile = leftOffset + Point(0, 49 - local.y)
                            }
                        }
                    }
                    position.x in 100..149 && position.y in 0..49 -> {
                        // Right
                        val local = position - rightOffset
                        when (facing) {
                            NORTH -> {
                                newFacing = NORTH
                                tile = backOffset + Point(local.x, 49)
                            }
                            EAST -> {
                                newFacing = WEST
                                tile = bottomOffset + Point(49, 49 - local.y)
                            }
                            SOUTH -> {
                                newFacing = WEST
                                tile = frontOffset + Point(49, local.x)
                            }
                            WEST -> TODO()
                        }
                    }
                    position.x in 50..99 && position.y in 50..99 -> {
                        // Front
                        val local = position - frontOffset
                        when (facing) {
                            NORTH -> TODO()
                            EAST -> {
                                newFacing = NORTH
                                tile = rightOffset + Point(local.y, 49)
                            }
                            SOUTH -> TODO()
                            WEST -> {
                                newFacing = SOUTH
                                tile = leftOffset + Point(local.y, 0)
                            }
                        }
                    }
                    position.x in 0..49 && position.y in 100..149 -> {
                        // Left
                        val local = position - leftOffset
                        when (facing) {
                            NORTH -> {
                                newFacing = EAST
                                tile = frontOffset + Point(0, local.x)
                            }
                            EAST -> TODO()
                            SOUTH -> TODO()
                            WEST -> {
                                newFacing = EAST
                                tile = topOffset + Point(0, 49 - local.y)
                            }
                        }
                    }
                    position.x in 50..99 && position.y in 100..149 -> {
                        // Bottom
                        val local = position - bottomOffset
                        when (facing) {
                            NORTH -> TODO()
                            EAST -> {
                                newFacing = WEST
                                tile = rightOffset + Point(49, 49 - local.y)
                            }
                            SOUTH -> {
                                newFacing = WEST
                                tile = backOffset + Point(49, local.x)
                            }
                            WEST -> TODO()
                        }
                    }
                    position.x in 0..49 && position.y in 150..199 -> {
                        // Back
                        val local = position - backOffset
                        when (facing) {
                            NORTH -> TODO()
                            EAST -> {
                                newFacing = NORTH
                                tile = bottomOffset + Point(local.y, 49)
                            }
                            SOUTH -> {
                                newFacing = SOUTH
                                tile = rightOffset + Point(local.x, 0)
                            }
                            WEST -> {
                                newFacing = SOUTH
                                tile = topOffset + Point(local.y, 0)
                            }
                        }
                    }
                }
            }
            if (map[tile] == '.') {
                position = tile
                facing = newFacing
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
                else -> throw RuntimeException()
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
