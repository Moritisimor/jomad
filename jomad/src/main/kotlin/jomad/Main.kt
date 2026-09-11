@file:Suppress("KotlinPrintToLogpoint")

package jomad
import jomad.interpreter.Interpreter
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val interpreter = Interpreter()

    if (args.isNotEmpty()) {
        if (args.contains("--help") || args.contains("-h")) {
            println(" \\\\")
            println("  \\\\")
            println(" //\\\\")
            println("//  \\\\")

            println("The Magnificent Jomad Lisp Interpretation System")
            println("https://github.com/Moritisimor/jomad")
            return
        }

        if (args[0] == "-e" || args[0] == "--evaluate") {
            if (args.size < 2) {
                println("Expected argument after evaluation-flag")
                exitProcess(1)
            }

            val sourceCode = args[1]
            interpreter.doString(sourceCode)
                .onSuccess { println(it) }
                .onFailure { println("Error: $it"); exitProcess(1) }

            return
        }

        val filePath = args[0]
        try {
            val sourceCode = File(filePath).readText()
            interpreter.doString(sourceCode)
                .onFailure { println("Error: $it"); exitProcess(1) }
        } catch (_: FileNotFoundException) {
            println("File not found: $filePath")
            exitProcess(1)
        }

        return
    }

    while (true) {
        print("Jomad λ ")
        val sourceCode = readlnOrNull()
        if (sourceCode == null) { // Ctrl + D
            println("Bye!")
            return
        }

        interpreter.doString(sourceCode).fold(
            { println("Evaluates to: $it") },
            { println("Error: $it") }
        )
    }
}
