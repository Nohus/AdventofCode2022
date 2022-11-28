
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

private fun getInputFile(): File {
    val name = Throwable().stackTrace.first { it.className.contains("day") }.fileName
    val day = name.substringBefore("_").removePrefix("Day").padStart(2, '0')
    val part = name.substringAfter("_").removeSuffix(".kt")
    val file = File("src/main/kotlin/day$day/input$part.txt")
    return if (file.readText().isBlank()) {
        File("src/main/kotlin/day$day/input1.txt")
    } else file
}

private fun getInput(): String {
    return getInputFile().readText().trim()
}

fun printInput(input: String) {
    if (input.contains("\n")) {
        if (input.lines().size >= 10) {
            println("Input:")
            println(input.lines().take(2).joinToString("\n"))
            println("[> not showing ${input.lines().size - 4} lines <]")
            println(input.lines().takeLast(2).joinToString("\n"))
        } else {
            println("Input:\n$input")
        }
    } else {
        println("Input: $input")
    }
}

@OptIn(ExperimentalTime::class)
fun solve(additionalTiming: Boolean = false, solve: (List<String>) -> Any?) {
    val input = getInput()
    printInput(input)
    val (answer, duration) = measureTimedValue {
        solve(input.lines()).toString()
    }
    val time = "${String.format("%.3f", duration.inWholeMicroseconds / 1000.0)}ms"
    if (answer != "kotlin.Unit") {
        println("Out: $answer [$time]")
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(answer), null)
        Thread.sleep(200) // Wait so the system has chance to notice the clipboard change
    } else {
        println("No answer [$time]")
    }

    if (additionalTiming) {
        println("Rerunning for timing")
        val count = if (duration < 100.milliseconds) 1000 else 10
        val fastest = List(count) {
            measureTime { solve(input.lines()) }
        }.minOrNull()!!
        println("Fastest time: [${String.format("%.3f", fastest.inWholeMicroseconds / 1000.0)}ms]")
    }
}
