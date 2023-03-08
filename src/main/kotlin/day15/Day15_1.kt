package day15

import solve
import utils.Direction
import utils.Point

fun main() = solve { lines ->
    val exclusion = mutableSetOf<Point>()
    lines.map { line ->
        val numbers = line.split('=', ',', ':').mapNotNull { it.toIntOrNull() }
        val sensor = Point(numbers[0], numbers[1])
        val beacon = Point(numbers[2], numbers[3])
        val distance = sensor.manhattan(beacon)
        var current = Point(sensor.x, 2000000)
        while (sensor.manhattan(current) <= distance) {
            exclusion += current
            current = current.move(Direction.WEST)
        }
        current = Point(sensor.x, 2000000)
        while (sensor.manhattan(current) <= distance) {
            exclusion += current
            current = current.move(Direction.EAST)
        }
        exclusion -= beacon
    }

    exclusion.size
}
