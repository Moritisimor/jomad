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

        println(
            interpreter.doString(sourceCode).fold(
                { it },
                { "Error: $it" }
            )
        )
    }
}
