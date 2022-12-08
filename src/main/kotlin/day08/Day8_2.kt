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
    trees.maxOf { (point, height) ->
        var left = ((point.x - 1) downTo 0).takeWhile { trees[Point(it, point.y)]!! < height }.size
        if ((0 until point.x).count() > left) left++
        var right = ((point.x + 1)..maxX).takeWhile { trees[Point(it, point.y)]!! < height }.size
        if (((point.x + 1)..maxX).count() > right) right++
        var up = ((point.y - 1) downTo 0).takeWhile { trees[Point(point.x, it)]!! < height }.size
        if ((0 until point.y).count() > up) up++
        var down = ((point.y + 1)..maxY).takeWhile { trees[Point(point.x, it)]!! < height }.size
        if (((point.y + 1)..maxY).count() > down) down++
        left * right * up * down
    }
}
