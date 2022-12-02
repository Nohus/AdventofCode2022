package day01

import solve

fun main() = solve { lines ->
    mutableListOf(0).also { elves ->
        lines.forEach { line ->
            if (line.isBlank()) elves += 0 else elves[elves.lastIndex] += line.toInt()
        }
    }.sorted().takeLast(3).sum()
}
