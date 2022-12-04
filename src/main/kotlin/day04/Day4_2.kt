package day04

import solve

fun main() = solve { lines ->
    lines.count { line ->
        val (a, b) = line.split(",").map { it.split("-").map { it.toInt() }.let { (a, b) -> (a..b) } }
        (a.first in b) || (b.first in a)
    }
}
