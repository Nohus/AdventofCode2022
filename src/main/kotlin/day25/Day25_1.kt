package day25

import solve
import kotlin.math.absoluteValue

fun main() = solve { lines ->

    fun power(base: Int, exponent: Int) = List(exponent) { base.toLong() }.fold(1L) { acc, num -> acc * num }

    fun snafuToDecimal(snafu: String): Long {
        val num = snafu.reversed().mapIndexed { column, char ->
            when (char) {
                '=' -> -2
                '-' -> -1
                else -> char.digitToInt()
            } * power(5, column)
        }
        return num.sum()
    }

    fun columnsToSnafu(columns: Map<Int, Pair<Int, Long>>): String {
        return columns.entries.sortedByDescending { it.key }.map { it.value.first }.map {
            when (it) {
                -2 -> '='
                -1 -> '-'
                else -> it.toString()[0]
            }
        }.joinToString("")
    }

    val sum = lines.sumOf { line -> snafuToDecimal(line) }

    data class Digit(
        val column: Int,
        val digit: Int,
        val value: Long,
        val newDelta: Long
    )
    var delta = -sum
    val columns = mutableMapOf<Int, Pair<Int, Long>>()
    val protectedColumns = mutableListOf<Int>()
    columns[0] = 0 to 0L
    while (delta != 0L) {
        val maxColumn = columns.keys.max()
        if (columns[maxColumn]!!.first != 0) {
            columns[maxColumn + 1] = (0 to 0L)
        }
        val possibleInAllColumns = columns.keys.flatMap { column ->
            listOf(-2, -1, 0, 1, 2).map { digit ->
                val digitValue = digit * power(5, column)
                val currentDigitValue = columns[column]?.second ?: 0
                val diff = digitValue - currentDigitValue
                Digit(column, digit, digitValue, delta + diff)
            }
        }
        val bestChange = possibleInAllColumns.filter { it.column !in protectedColumns }.minBy { it.newDelta.absoluteValue }
        if (bestChange.newDelta == delta) {
            val columnToIncrease = columns.entries.filter { it.value.first < 2 }.minOf { it.key }
            val existingValue = columns[columnToIncrease]!!
            val newDigit = existingValue.first + 1
            val newValue = newDigit * power(5, columnToIncrease)
            val new = newDigit to newValue
            columns[columnToIncrease] = new
            delta += newValue - existingValue.second
            protectedColumns += columnToIncrease
        } else {
            columns[bestChange.column] = bestChange.digit to bestChange.value
            delta = bestChange.newDelta
        }
    }

    columnsToSnafu(columns).drop(1)
}
