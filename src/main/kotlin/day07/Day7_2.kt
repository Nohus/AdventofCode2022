package day07

import solve

fun main() = solve { lines ->
    class Directory(val parent: Directory?, var children: List<Directory> = emptyList()) {
        val totalSize: Long get() = filesSize + children.sumOf { it.totalSize }
        var filesSize = 0L
    }

    val directories = mutableListOf(Directory(null))
    var current: Directory = directories.first()
    lines.drop(1).forEach { line ->
        when {
            line == "$ cd .." -> current = current.parent!!
            line.startsWith("$ cd") -> Directory(current).also { current.children += it; directories += it; current = it }
            line[0].isDigit() -> current.filesSize += line.substringBefore(" ").toLong()
        }
    }

    val missingSpace = 30000000 - (70000000 - directories.first().totalSize)
    directories.filter { it.totalSize >= missingSpace }.minOf { it.totalSize }
}
