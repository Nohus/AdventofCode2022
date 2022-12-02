package day02

import solve

fun main() = solve { lines ->
    val win = mapOf('A' to 'Y', 'B' to 'Z', 'C' to 'X')
    val draw = mapOf('A' to 'X', 'B' to 'Y', 'C' to 'Z')
    val lose = mapOf('A' to 'Z', 'B' to 'X', 'C' to 'Y')
    lines.sumOf { line ->
        if (line[2] == 'Z') win[line[0]]!!.code - 'X'.code + 1 + 6
        else if (line[2] == 'Y') draw[line[0]]!!.code - 'X'.code + 1 + 3
        else lose[line[0]]!!.code - 'X'.code + 1
    }
}
