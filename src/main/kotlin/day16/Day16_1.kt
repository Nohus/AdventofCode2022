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

    fun simulate(order: List<String>): Int {
        var minute = 0
        var totalFlow = 0
        var pressure = 0
        for ((from, to) in order.windowed(2)) {
            val path = search(from, to)
            val steps = path.size - 1
            if (minute + steps >= 30) break
            minute += steps
            pressure += totalFlow * steps

            val atNow = valves[to]!!
            minute++
            pressure += totalFlow
            totalFlow += atNow.flow
        }
        pressure += totalFlow * (30 - minute)
        return pressure
    }

    val flowValves = valves.values.filter { it.flow > 0 } + valves.values.first { it.name == "AA" }
    fun getAllPaths(from: String, visited: List<String>, timeLeft: Int): List<List<String>> {
        if (timeLeft <= 0) return emptyList()
        val connections = flowValves.map { it.name } - from
        val unvisited = connections.filter { it !in visited }
        return unvisited.flatMap { connection ->
            val time = search(from, connection).size - 1
            val further = getAllPaths(connection, visited + from, timeLeft - time).map { path ->
                listOf(from) + path
            }
            if (further.isNotEmpty()) further else listOf(listOf(from, connection))
        }
    }
    val x = getAllPaths("AA", emptyList(), 30)
    x.map { it to simulate(it) }.maxBy { it.second }
}
