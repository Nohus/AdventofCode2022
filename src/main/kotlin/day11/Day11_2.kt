package day11

import solve
import java.math.BigInteger

fun main() = solve { lines ->
    data class Monkey(
        val items: MutableList<BigInteger>,
        val operation: (BigInteger) -> BigInteger,
        val test: (BigInteger) -> Boolean,
        val ifTrue: Int,
        val ifFalse: Int,
        var activity: Long = 0
    )
    val monkeys = listOf(
        Monkey(mutableListOf(BigInteger.valueOf(66), BigInteger.valueOf(79)), {it*BigInteger.valueOf(11)}, {it%BigInteger.valueOf(7)==BigInteger.ZERO}, 6, 7),
        Monkey(mutableListOf(BigInteger.valueOf(84), BigInteger.valueOf(94), BigInteger.valueOf(94), BigInteger.valueOf(81), BigInteger.valueOf(98), BigInteger.valueOf(75)), {it*BigInteger.valueOf(17)}, {it%BigInteger.valueOf(13)==BigInteger.ZERO}, 5, 2),
        Monkey(mutableListOf(BigInteger.valueOf(85), BigInteger.valueOf(79), BigInteger.valueOf(59), BigInteger.valueOf(64), BigInteger.valueOf(79), BigInteger.valueOf(95), BigInteger.valueOf(67)), {it+BigInteger.valueOf(8)}, {it%BigInteger.valueOf(5)==BigInteger.ZERO}, 4, 5),
        Monkey(mutableListOf(BigInteger.valueOf(70)), {it+BigInteger.valueOf(3)}, {it%BigInteger.valueOf(19)==BigInteger.ZERO}, 6, 0),
        Monkey(mutableListOf(BigInteger.valueOf(57), BigInteger.valueOf(69), BigInteger.valueOf(78), BigInteger.valueOf(78)), {it+BigInteger.valueOf(4)}, {it%BigInteger.valueOf(2)==BigInteger.ZERO}, 0, 3),
        Monkey(mutableListOf(BigInteger.valueOf(65), BigInteger.valueOf(92), BigInteger.valueOf(60), BigInteger.valueOf(74), BigInteger.valueOf(72)), {it+BigInteger.valueOf(7)}, {it%BigInteger.valueOf(11)==BigInteger.ZERO}, 3, 4),
        Monkey(mutableListOf(BigInteger.valueOf(77), BigInteger.valueOf(91), BigInteger.valueOf(91)), {it*it}, {it%BigInteger.valueOf(17)==BigInteger.ZERO}, 1, 7),
        Monkey(mutableListOf(BigInteger.valueOf(76), BigInteger.valueOf(58), BigInteger.valueOf(57), BigInteger.valueOf(55), BigInteger.valueOf(67), BigInteger.valueOf(77), BigInteger.valueOf(54), BigInteger.valueOf(99)), {it+BigInteger.valueOf(6)}, {it%BigInteger.valueOf(3)==BigInteger.ZERO}, 2, 1)
    )
    val biggestMultiple = BigInteger.valueOf(7*13*5*19*2*11*17*3)
    for (round in 1..10_000) {
        monkeys.forEachIndexed { index, monkey ->
            while (monkey.items.isNotEmpty()) {
                var item = monkey.items.removeFirst()
                monkey.activity++
                item = monkey.operation(item)
                item %= biggestMultiple
                if (monkey.test(item)) monkeys[monkey.ifTrue].items += item
                else monkeys[monkey.ifFalse].items += item
            }
        }
    }
    monkeys.sortedByDescending { it.activity }.take(2).map { it.activity }.let { it[0] * it [1] }
}
