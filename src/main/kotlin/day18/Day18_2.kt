package day18

import solve
import utils.Vector3

fun main() = solve { lines ->
    val cubes = lines.map { it.split(",").map { it.toInt() }.let { Vector3(it[0], it[1], it[2]) } }
    val xRange = (cubes.minOf { it.x } + 1)..<cubes.maxOf { it.x }
    val yRange = (cubes.minOf { it.y } + 1)..<cubes.maxOf { it.y }
    val zRange = (cubes.minOf { it.z } + 1)..<cubes.maxOf { it.z }
    val trapped = mutableSetOf<Vector3>()
    for (x in xRange) {
        for (y in yRange) {
            for (z in zRange) {
                val checking = Vector3(x, y, z)
                if (checking in cubes || checking in trapped) continue
                val reached = cubes.toMutableSet()
                val current = mutableSetOf(checking)
                reached += checking
                while (true) {
                    current.toList().forEach {
                        current -= it
                        val expanded = it.getAdjacentSides().filter { it !in reached }
                        reached += expanded
                        current += expanded
                    }
                    if (current.any { it.x !in xRange || it.y !in yRange || it.z !in zRange }) break
                    if (current.isEmpty()) {
                        trapped += reached
                        break
                    }
                }
            }
        }
    }
    cubes.sumOf { a -> a.getAdjacentSides().filter { side -> side !in cubes && side !in trapped }.size }
}
