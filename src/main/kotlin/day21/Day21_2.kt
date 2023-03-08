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
    resolved.remove("humn")
    while (true) {
        val possible = waiting.entries.firstOrNull { it.value.a in resolved && it.value.b in resolved } ?: break
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
    val root = waiting["root"]!!
    val requiredValue = if (root.a in resolved) resolved[root.a]!! else resolved[root.b]!!
    val requiredName = if (root.a in resolved) root.b else root.a
    fun need(name: String, toBe: Long): Long {
        if (name == "humn") return toBe
        val operation = waiting[name]!!
        val known = if (operation.a in resolved) resolved[operation.a]!! else resolved[operation.b]!!
        val other = if (operation.a in resolved) operation.b else operation.a
        return when (operation.operation) {
            '+' -> need(other, toBe - known)
            '-' -> if (other == operation.b) need(other, -toBe + known) else need(other, toBe + known)
            '/' -> if (other == operation.b) need(other, known / toBe) else need(other, toBe * known)
            '*' -> need(other, toBe / known)
            else -> throw RuntimeException()
        }
    }
    need(requiredName, requiredValue)
}
