package day03

import solve

fun main() = solve { lines ->
    lines.sumOf { sack ->
        val (a, b) = sack.chunked(sack.length / 2)
        val item = a.first { it in b }
        if (item.isUpperCase()) item.code - 'A'.code + 27 else item.code - 'a'.code + 1
    }
}
