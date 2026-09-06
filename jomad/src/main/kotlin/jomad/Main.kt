package jomad

import jomad.interpreter.Interpreter

fun main() {
    val interpreter = Interpreter()
    while (true) {
        try {
            print("Jomad λ ")
            val sourceCode = readln()
            println(interpreter.doString(sourceCode).fold(
                { it },
                { "Error: $it" }
            ))
        } catch (_: Exception) {
            println("Goodbye!")
            return
        }
    }
}
