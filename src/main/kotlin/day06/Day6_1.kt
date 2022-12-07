package day06

import solve

fun main() = solve { lines ->
    lines.first().windowed(4).indexOfFirst { it.toSet().size == 4 } + 4
}
