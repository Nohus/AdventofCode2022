package day17

import solve
import utils.Direction.*
import utils.Point

fun main() = solve { lines ->
    val shapes = listOf(
        listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0)),
        listOf(Point(1, 0), Point(0, 1), Point(1, 1), Point(2, 1), Point(1, 2)),
        listOf(Point(2, 0), Point(2, 1), Point(0, 2), Point(1, 2), Point(2, 2)),
        listOf(Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3)),
        listOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1))
    )
    val moves = lines.first().toCharArray().map { if (it == '>') EAST else WEST }
    var currentShapeIndex = 0
    var currentMoveIndex = 0
    val tower = mutableSetOf<Point>()

    repeat(2022) {
        val shape = shapes[currentShapeIndex++]
        if (currentShapeIndex > shapes.lastIndex) currentShapeIndex = 0
        val towerHeight = tower.minOfOrNull { it.y } ?: 0
        val spawnPoint = Point(2, towerHeight - 4 - shape.maxOf { it.y })
        var spawnedShape = shape.map { spawnPoint + it }
        while (true) {
            val move = moves[currentMoveIndex++]
            if (currentMoveIndex > moves.lastIndex) currentMoveIndex = 0
            if (spawnedShape.all { it.move(move).x in 0..6 && !tower.contains(it.move(move)) }) {
                spawnedShape = spawnedShape.map { it.move(move) }
            }
            if (spawnedShape.all { it.move(SOUTH).y <= 0 && !tower.contains(it.move(SOUTH)) }) {
                spawnedShape = spawnedShape.map { it.move(SOUTH) }
            } else {
                tower += spawnedShape
                break
            }
        }
    }
    -tower.minOf { it.y } + 1
}
