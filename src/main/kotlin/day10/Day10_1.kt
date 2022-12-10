package day10

import solve

fun main() = solve { lines ->
    var x = 1
    var cycle = 1
    var sum = 0

    fun processCycle() {
        if (cycle % 40 == 20) sum += cycle * x
        cycle++
    }

    lines.forEach { line ->
        if (line == "noop") processCycle()
        else {
            processCycle(); processCycle()
            x += line.substringAfter(" ").toInt()
        }
    }
    sum
}
