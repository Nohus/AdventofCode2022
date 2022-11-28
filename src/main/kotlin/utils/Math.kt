package utils

fun <T> List<T>.getPermutations(): List<List<T>> {
    return if (size == 2) listOf(listOf(this[0], this[1]), listOf(this[1], this[0]))
    else map { initial ->
        (this - initial).getPermutations().map { permutation -> listOf(initial) + permutation }
    }.flatten()
}

fun leastCommonMultiple(a: Int, b: Int): Int {
    return a * b / greatestCommonDivisor(a, b)
}

tailrec fun greatestCommonDivisor(a: Int, b: Int): Int {
    return if (b == 0) a
    else greatestCommonDivisor(b, a % b)
}

tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) a
    else greatestCommonDivisor(b, a % b)
}
