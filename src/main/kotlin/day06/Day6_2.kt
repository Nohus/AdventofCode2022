package day06

import solve

fun main() = solve { lines ->
    lines.first().windowed(14).indexOfFirst { it.toSet().size == 14 } + 14
}
