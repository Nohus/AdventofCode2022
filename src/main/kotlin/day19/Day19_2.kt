package day19

import solve

enum class Robot { Ore, Clay, Obsidian, Geode }

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
        val num = line.split(" ").mapNotNull { it.toIntOrNull() }
        Blueprint(num[0], num[1], num[2], num[3], num[4], num[5])
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
        unvisited += emptyList<Robot?>() to State()
        while (unvisited.isNotEmpty()) {
            val next = unvisited.removeFirst()
            val state = next.second
            val minute = next.first.size + 1
            val minutesLeft = 32 - minute

            if (unvisited.any { it.first.size == next.first.size && it.second.isBetterThan(state) }) continue
            val maxNeededOre = maxOf(blueprint.oreOre, blueprint.clayOre, blueprint.obsidianOre, blueprint.geodeOre) * minutesLeft
            if (state.ore >= maxNeededOre && next.first.last() == Robot.Ore) continue
            val maxNeededClay = blueprint.obsidianClay * minutesLeft
            if (state.clay >= maxNeededClay && next.first.last() == Robot.Clay) continue
            if (state.oreRobots > maxOf(blueprint.clayOre, blueprint.obsidianOre, blueprint.geodeOre)) continue
            if (state.clayRobots > blueprint.obsidianClay) continue

            val buildableRobots = Robot.values().filter { robot ->
                when (robot) {
                    Robot.Ore -> state.ore >= blueprint.oreOre
                    Robot.Clay -> state.ore >= blueprint.clayOre
                    Robot.Obsidian -> state.ore >= blueprint.obsidianOre && state.clay >= blueprint.obsidianClay
                    Robot.Geode -> state.ore >= blueprint.geodeOre && state.obsidian >= blueprint.geodeObsidian
                }
            }
            val newState = state.copy(
                ore = state.ore + state.oreRobots,
                clay = state.clay + state.clayRobots,
                obsidian = state.obsidian + state.obsidianRobots,
                geodes = state.geodes + state.geodeRobots
            )

            val maxGeodes = newState.geodes + List(minutesLeft) { newState.geodeRobots + (it + 1) }.sum()
            if (unvisited.any { it.second.geodes > maxGeodes }) continue

            if (minutesLeft == 0) {
                results += (next.first + null) to newState
                continue
            }

            var possibleSteps = listOf<Robot?>(null) + buildableRobots
            if (Robot.Geode in possibleSteps) possibleSteps = listOf(Robot.Geode)
            val outcomes = possibleSteps.map { step ->
                val option = when (step) {
                    Robot.Ore -> newState.copy(ore = newState.ore - blueprint.oreOre, oreRobots = newState.oreRobots + 1)
                    Robot.Clay -> newState.copy(ore = newState.ore - blueprint.clayOre, clayRobots = newState.clayRobots + 1)
                    Robot.Obsidian -> newState.copy(ore = newState.ore - blueprint.obsidianOre, clay = newState.clay - blueprint.obsidianClay, obsidianRobots = newState.obsidianRobots + 1)
                    Robot.Geode -> newState.copy(ore = newState.ore - blueprint.geodeOre, obsidian = newState.obsidian - blueprint.geodeObsidian, geodeRobots = newState.geodeRobots + 1)
                    null -> newState
                }
                (next.first + step) to option
            }
            unvisited += outcomes
        }
        return results.maxOf { it.second.geodes }
    }

    blueprints.take(3).fold(1) { acc, blueprint ->
        acc * search(blueprint)
    }
}
