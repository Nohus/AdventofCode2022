package day19

import day19.Robot.*
import solve
import kotlin.math.min

fun main() = solve { lines ->
    data class Blueprint(
        val oreOre: Int,
        val clayOre: Int,
        val obsidianOre: Int,
        val obsidianClay: Int,
        val geodeOre: Int,
        val geodeObsidian: Int
    )
    val blueprints = lines.map { line ->
        val oreOre = line.substringBefore(" ore.").substringAfter("costs ").toInt()
        val clayOre = line.substringAfter("clay robot").substringBefore(" ore").substringAfter("costs ").toInt()
        val obsidianOre = line.substringAfter("obsidian robot").substringBefore(" ore").substringAfter("costs ").toInt()
        val obsidianClay = line.substringAfter("obsidian robot").substringBefore(" clay").substringAfter("and ").toInt()
        val geodeOre = line.substringAfter("geode robot").substringBefore(" ore").substringAfter("costs ").toInt()
        val geodeObsidian = line.substringAfter("geode robot").substringBefore(" obsidian").substringAfter("and ").toInt()
        Blueprint(oreOre, clayOre, obsidianOre, obsidianClay, geodeOre, geodeObsidian)
    }

    data class State(
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0
    )

    fun State.isBetterThan(other: State): Boolean {
        return ore >= other.ore && clay >= other.clay && obsidian >= other.obsidian && geodes >= other.geodes
                && oreRobots >= other.oreRobots && clayRobots >= other.clayRobots
                && obsidianRobots >= other.obsidianRobots && geodeRobots >= other.geodeRobots
    }

    fun search(blueprint: Blueprint): Int {
        val results = mutableListOf<Pair<List<Robot?>, State>>()
        val unvisited = mutableListOf<Pair<List<Robot?>, State>>()
        val visited = mutableListOf<Pair<List<Robot?>, State>>()
        unvisited += emptyList<Robot?>() to State()
        while (unvisited.isNotEmpty()) {
            val next = unvisited.removeFirst()
            visited += next
            val state = next.second
            val minute = next.first.size + 1
            val minutesLeft = 24 - minute

            if (state.ore >= blueprint.geodeOre * 2 && state.obsidian >= blueprint.geodeObsidian * 2) continue
            val better = unvisited.firstOrNull { it.first.size == next.first.size && it.second.isBetterThan(state) }
            if (better != null) {
                continue
            }
            val maxNeededOre = maxOf(blueprint.oreOre, blueprint.clayOre, blueprint.obsidianOre, blueprint.geodeOre) * minutesLeft
            if (state.ore >= maxNeededOre && next.first.last() == Ore) continue
            val maxNeededClay = blueprint.obsidianClay * minutesLeft
            if (state.clay >= maxNeededClay && next.first.last() == Clay) continue
            if (state.oreRobots > maxOf(blueprint.clayOre, blueprint.obsidianOre, blueprint.geodeOre)) continue

            val buildableRobots = values().filter { robot ->
                when (robot) {
                    Ore -> state.ore >= blueprint.oreOre
                    Clay -> state.ore >= blueprint.clayOre
                    Obsidian -> state.ore >= blueprint.obsidianOre && state.clay >= blueprint.obsidianClay
                    Geode -> state.ore >= blueprint.geodeOre && state.obsidian >= blueprint.geodeObsidian
                }
            }
            val newState = state.copy(
                ore = state.ore + state.oreRobots,
                clay = state.clay + state.clayRobots,
                obsidian = state.obsidian + state.obsidianRobots,
                geodes = state.geodes + state.geodeRobots
            )

            val maxGeodes = newState.geodes + List(minutesLeft) {
                newState.geodeRobots + (it + 1)
            }.sum()
            val betterGeodes = unvisited.firstOrNull { it.second.geodes > maxGeodes }
            if (betterGeodes != null) continue

            if (minute == 24) {
                results += (next.first + null) to newState
                continue
            }

            val possibleSteps = listOf<Robot?>(null) + buildableRobots
            val outcomes = possibleSteps.map { step ->
                val option = when (step) {
                    Ore -> newState.copy(ore = newState.ore - blueprint.oreOre, oreRobots = newState.oreRobots + 1)
                    Clay -> newState.copy(ore = newState.ore - blueprint.clayOre, clayRobots = newState.clayRobots + 1)
                    Obsidian -> newState.copy(ore = newState.ore - blueprint.obsidianOre, clay = newState.clay - blueprint.obsidianClay, obsidianRobots = newState.obsidianRobots + 1)
                    Geode -> newState.copy(ore = newState.ore - blueprint.geodeOre, obsidian = newState.obsidian - blueprint.geodeObsidian, geodeRobots = newState.geodeRobots + 1)
                    null -> newState
                }
                (next.first + step) to option
            }
            unvisited += outcomes
        }
        val best = results.maxBy { it.second.geodes }
        return best.second.geodes
    }

    blueprints.withIndex().sumOf { (index, blueprint) ->
        (index + 1) * search(blueprint)
    }
}
