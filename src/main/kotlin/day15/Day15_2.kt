package day15

import solve
import utils.Point

fun main() = solve { lines ->
    val range = 0..4000000
    lines.associate { it.split('=', ',', ':').mapNotNull(String::toIntOrNull)
            .let { num -> Point(num[0], num[1]).let { it to it.manhattan(Point(num[2], num[3])) + 1 } }
    }.let { sensors -> sensors.firstNotNullOf { (sensor, distance) -> (-distance..distance).firstNotNullOfOrNull { x ->
            (sensor + Point(x, distance + x)).takeIf { p -> p.x in range && p.y in range && sensors.all { it.key.manhattan(p) >= it.value - 1 } }
        } }.let { it.x.toLong() * range.last + it.y }
    }
}
