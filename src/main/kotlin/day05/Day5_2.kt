package day05

import solve

fun main() = solve(trim = false) { lines ->
    val crates = (1..9).associateWith { emptyList<Char>() }.toMutableMap()
    lines.filter { '[' in it }.forEach { line ->
        for (stackIndex in 1..9) {
            val lineIndex = 1 + (stackIndex - 1) * 4
            if (line.lastIndex < lineIndex || line[lineIndex] == ' ') continue
            crates[stackIndex] = listOf(line[lineIndex]) + crates.getValue(stackIndex)
        }
    }
    lines.filter { "move" in it }.forEach { line ->
        val (amount, from, to) = "\\d+".toRegex().findAll(line).map { it.value.toInt() }.toList()
        crates[to] = crates.getValue(to) + crates.getValue(from).takeLast(amount)
        crates[from] = crates.getValue(from).dropLast(amount)
    }
    crates.values.map { it.last() }.joinToString("")
}
