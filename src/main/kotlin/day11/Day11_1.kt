package day11

import solve

fun main() = solve { lines ->
    data class Monkey(
        val items: MutableList<Int>,
        val operation: (Int) -> Int,
        val test: (Int) -> Boolean,
        val ifTrue: Int,
        val ifFalse: Int,
        var activity: Int = 0
    )
    val monkeys = listOf(
        Monkey(mutableListOf(66, 79), {it*11}, {it%7==0}, 6, 7),
        Monkey(mutableListOf(84, 94, 94, 81, 98, 75), {it*17}, {it%13==0}, 5, 2),
        Monkey(mutableListOf(85, 79, 59, 64, 79, 95, 67), {it+8}, {it%5==0}, 4, 5),
        Monkey(mutableListOf(70), {it+3}, {it%19==0}, 6, 0),
        Monkey(mutableListOf(57, 69, 78, 78), {it+4}, {it%2==0}, 0, 3),
        Monkey(mutableListOf(65, 92, 60, 74, 72), {it+7}, {it%11==0}, 3, 4),
        Monkey(mutableListOf(77, 91, 91), {it*it}, {it%17==0}, 1, 7),
        Monkey(mutableListOf(76, 58, 57, 55, 67, 77, 54, 99), {it+6}, {it%3==0}, 2, 1)
    )
    for (round in 1..20) {
        monkeys.forEachIndexed { index, monkey ->
            while (monkey.items.isNotEmpty()) {
                var item = monkey.items.removeFirst()
                monkey.activity++
                item = monkey.operation(item)
                item /= 3
                if (monkey.test(item)) {
                    monkeys[monkey.ifTrue].items += item
                } else {
                    monkeys[monkey.ifFalse].items += item
                }
            }
        }
    }
    monkeys.sortedByDescending { it.activity }.take(2).map { it.activity }.let { it[0] * it [1] }
}
