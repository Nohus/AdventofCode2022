package day16

import solve

fun main() = solve { lines ->
    data class Valve(val name: String, val flow: Int, val connects: List<String>)
    val valves = lines.associate {
        val valve = it.substringAfter("Valve ").substringBefore(" ")
        val flow = it.substringAfter("rate=").substringBefore(";").toInt()
        val connects = it.substringAfter("to valve").substringAfter(" ").split(", ")
        valve to Valve(valve, flow, connects)
    }

    val searchedCache = mutableMapOf<Pair<String, String>, List<String>>()
    fun search(from: String, target: String): List<String> {
        if (searchedCache.containsKey(from to target)) return searchedCache[from to target]!!
        val visited = mutableListOf<String>()
        val queue = mutableListOf<String>()
        val parents = mutableMapOf<String, String>()
        visited += from
        queue += from
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next == target) {
                val path = mutableListOf<String>()
                path += target
                while (true) {
                    val prev = parents[path.last()]!!
                    path += prev
                    if (prev == from) break
                }
                return path.reversed().also { searchedCache[from to target] = it }
            }
            valves[next]!!.connects.filterNot { it in visited }.forEach { neighbor ->
                visited += neighbor
                queue += neighbor
                parents[neighbor] = next
            }
        }
        throw RuntimeException()
    }

    fun simulate(order: List<String>, order2: List<String>): Int {
        var elapsed1 = 0
        var elapsed2 = 0
        var pressure = 0
        val opens = mutableListOf<Pair<String, Int>>()
        for ((from, to) in order.windowed(2)) {
            val path = search(from, to)
            val steps = path.size - 1
            if (elapsed1 + steps >= 26) break
            elapsed1 += steps

            val atNow = valves[to]!!
            elapsed1++
            opens += atNow.name to elapsed1
        }
        for ((from, to) in order2.windowed(2)) {
            val path = search(from, to)
            val steps = path.size - 1
            if (elapsed2 + steps >= 26) break
            elapsed2 += steps

            val atNow = valves[to]!!
            elapsed2++
            opens += atNow.name to elapsed2
        }
        for (minute in 1 until 26) {
            val openValves = opens.filter { it.second <= minute }
            val flow = openValves.map { valves[it.first]!!.flow }.sum()
            pressure += flow
        }
        return pressure
    }

    val flowValves = valves.values.filter { it.flow > 0 } + valves.values.first { it.name == "AA" }
    fun getAllPaths(from: String, visited: List<String>, timeLeft: Int): List<List<String>> {
        if (timeLeft <= 0) return emptyList()
        val connections = if ("TA" !in visited && from == "AA") listOf("TA") else flowValves.map { it.name } - from
        val unvisited = connections.filter { it !in visited }
        return unvisited.flatMap { connection ->
            val time = search(from, connection).size - 1
            val further = getAllPaths(connection, visited + from, timeLeft - time).map { path ->
                listOf(from) + path
            }
            if (further.isNotEmpty()) further else listOf(listOf(from, connection))
        }
    }
    val x = getAllPaths("AA", emptyList(), 26)
    val solutions = x.map { path ->
        path to simulate(path, emptyList())
    }
    val best = solutions.maxBy { it.second }
    val b2 = solutions
        .filter { it.first.filter { it in flowValves.map { it.name } }.none { it in best.first } }
        .maxBy { it.second }
    best.second + b2.second
}
