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

    data class State(
        val pattern: List<Point>,
        val shapeIndex: Int,
        val moveIndex: Int,
        val patternHeight: Int,
        val rocksCount: Int
    )
    val states = mutableListOf<State>()
    var lastAddedHighest = 0
    var rocksCount = 0
    var finalRocksToFall: Long? = null
    var finalRockCyclesHeightGain: Long? = null
    while (true) {
        rocksCount++
        val towerHeight = tower.minOfOrNull { it.y } ?: 0

        if (finalRocksToFall == null) {
            val patternHeight = 10
            val patternHighest = towerHeight + patternHeight
            if (patternHighest < -(patternHeight * 2) && towerHeight != lastAddedHighest) {
                val topPattern = tower.filter { it.y in (patternHighest - patternHeight + 1)..(patternHighest) }
                val patternBottom = topPattern.maxOf { it.y }
                val patternTop = topPattern.minOf { it.y }
                val state = State(topPattern.map { Point(it.x, it.y - patternBottom) }, currentShapeIndex, currentMoveIndex, patternTop, rocksCount)
                val match = states.firstOrNull { it.pattern == state.pattern && it.shapeIndex == state.shapeIndex && it.moveIndex == state.moveIndex }
                if (match != null) {
                    val rocksCycle = rocksCount - match.rocksCount
                    val remainingRocksToFall = 1000000000000L - rocksCount + 1
                    finalRocksToFall = remainingRocksToFall % rocksCycle
                    finalRockCyclesHeightGain = (remainingRocksToFall / rocksCycle) * (match.patternHeight - patternTop)
                } else {
                    states += state
                    lastAddedHighest = towerHeight
                }
            }
        }

        val shape = shapes[currentShapeIndex++]
        if (currentShapeIndex > shapes.lastIndex) currentShapeIndex = 0
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
                if (finalRocksToFall != null) {
                    finalRocksToFall--
                    if (finalRocksToFall == 0L) return@solve -(tower.minOf { it.y }) + 1 + finalRockCyclesHeightGain!!
                }
                break
            }
        }
    }
}
