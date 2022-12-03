package day03

import solve

fun main() = solve { lines ->
    lines.chunked(3) { group ->
        val item = group.joinToString("").first { item -> group.all { item in it } }
        if (item.isUpperCase()) item.code - 'A'.code + 27 else item.code - 'a'.code + 1
    }.sum()
}
