package day13

import solve
import kotlin.math.max

fun main() = solve { lines ->

    abstract class Item
    data class Value(val value: Int): Item()
    data class Group(val items: List<Item>): Item()

    fun parseGroup(line: String): Group {
        var remaining = line.removePrefix("[").removeSuffix("]")
        val items = mutableListOf<Item>()
        while (remaining.isNotEmpty()) {
            if (remaining.first() == '[') {
                var open = 0
                val innerGroup = remaining.takeWhile {
                    if (it == '[') open++ else if (it == ']') open--
                    open > 0
                }
                items += parseGroup(innerGroup)
                remaining = remaining.drop(innerGroup.length + 2)
            } else {
                items += Value(remaining.substringBefore(",").toInt())
                remaining = remaining.substringAfter(",", "")
            }
        }
        return Group(items)
    }

    fun compareItems(a: Item, b: Item): Int {
        if (a is Value && b is Value) return compareValues(a.value, b.value)
        else if (a is Value && b is Group) return compareItems(Group(listOf(a)), b)
        else if (a is Group && b is Value) return compareItems(a, Group(listOf(b)))
        else if (a is Group && b is Group) {
            for (index in 0..<max(a.items.size, b.items.size)) {
                if (index > a.items.lastIndex) return -1
                if (index > b.items.lastIndex) return 1
                compareItems(a.items[index], b.items[index]).let { if (it != 0) return it }
            }
            return 0
        } else throw IllegalStateException()
    }

    lines.filter { it.isNotBlank() }.chunked(2).mapIndexed { index, (a, b) ->
        index to compareItems(parseGroup(a), parseGroup(b))
    }.filter { it.second == -1 }.sumOf { it.first + 1 }
}
