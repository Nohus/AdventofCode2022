package day18

import solve
import utils.Vector3

fun main() = solve { lines ->
    val cubes = lines.map { it.split(",").map { it.toInt() }.let { Vector3(it[0], it[1], it[2]) } }
    cubes.sumOf { a -> a.getAdjacentSides().filter { it !in cubes }.size }
}
