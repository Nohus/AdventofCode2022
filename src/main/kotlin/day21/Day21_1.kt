package day21

import solve

fun main() = solve { lines ->
    data class Operation(val a: String, val b: String, val operation: Char)
    val resolved = mutableMapOf<String, Long>()
    val waiting = mutableMapOf<String, Operation>()
    lines.forEach { line ->
        val name = line.substringBefore(":")
        val second = line.substringAfter(" ")
        if (second.toLongOrNull() != null) resolved[name] = second.toLong()
        else {
            val parts = second.split(' ')
            waiting[name] = Operation(parts[0], parts[2], parts[1][0])
        }
    }
    while (waiting.containsKey("root")) {
        val possible = waiting.entries.first { (_, operation) -> operation.a in resolved && operation.b in resolved }
        waiting.remove(possible.key)
        val a = resolved[possible.value.a]!!
        val b = resolved[possible.value.b]!!
        val result = when (possible.value.operation) {
            '+' -> a + b
            '-' -> a - b
            '/' -> a / b
            '*' -> a * b
            else -> throw RuntimeException()
        }
        resolved[possible.key] = result
    }
    resolved["root"]
}
