package jomad
import jomad.interpreter.Interpreter

fun main() {
    val interpreter = Interpreter()
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
