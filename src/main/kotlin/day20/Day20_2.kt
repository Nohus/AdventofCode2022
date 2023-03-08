package day20

import solve
import kotlin.math.absoluteValue

fun main() = solve { lines ->
    fun wrap(index: Int, move: Long, size: Int): Int {
        var normalized = move
        if (normalized < 0) normalized += size * ((normalized.absoluteValue / size) + 1)
        if (normalized > size) normalized -= size * ((normalized - size) / size + 1)
        var newIndex = index + normalized
        if (newIndex > size) newIndex -= size
        return newIndex.toInt()
    }
    var numbers = lines.mapIndexed { index, number -> index to number.toLong() * 811589153 }
    repeat(10) {
        repeat(numbers.size) { index ->
            val number = numbers.first { it.first == index }
            val newIndex = wrap(numbers.indexOf(number), number.second, numbers.lastIndex)
            val without = numbers.filterNot { it == number }
            numbers = without.take(newIndex) + number + without.drop(newIndex)
        }
    }
    val zeroIndex = numbers.indexOfFirst { it.second == 0L }
    listOf(1000, 2000, 3000).sumOf { numbers[(zeroIndex + it) % numbers.size].second }
}
