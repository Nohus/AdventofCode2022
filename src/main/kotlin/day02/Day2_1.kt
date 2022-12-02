package day02

import solve

fun main() = solve { lines ->
    val win = mapOf('A' to 'Y', 'B' to 'Z', 'C' to 'X')
    lines.sumOf { line ->
        line[2].code - 'X'.code + 1 + if (win[line[0]] == line[2]) 6 else 0 + if (line[2] - line[0] == ('X' - 'A')) 3 else 0
    }
}
