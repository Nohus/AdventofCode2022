package day08

import solve
import utils.Point

fun main() = solve { lines ->
    val trees = mutableMapOf<Point, Int>()
    lines.forEachIndexed { y, row ->
        row.forEachIndexed { x, height ->
            trees[Point(x, y)] = height.digitToInt()
        }
    }
    val (maxX, maxY) = trees.keys.last()
    trees.count { (point, height) ->
        if (point.x == 0 || point.y == 0 || point.x == maxX || point.y == maxY) return@count true
        if ((0..<point.x).all { trees[Point(it, point.y)]!! < height }) return@count true
        if (((point.x + 1)..maxX).all { trees[Point(it, point.y)]!! < height }) return@count true
        if ((0..<point.y).all { trees[Point(point.x, it)]!! < height }) return@count true
        if (((point.y + 1)..maxY).all { trees[Point(point.x, it)]!! < height }) return@count true
        false
    }
}
