@file:Suppress("KotlinPrintToLogpoint")
package com.github.moritisimor.jomad

import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.interpreter.Interpreter
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newList
import com.github.moritisimor.jomad.values.newString
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
            try {
                IO.println(interpreter.doStringOrThrow(sourceCode))
            } catch (e: EvaluationException) {
                println("Error: ${e.message}")
                e.printCallStack()
                e.printStackTrace()
                exitProcess(1)
            } catch (e: Throwable) {
                println("Error: ${e.message}")
                exitProcess(1)
            }

            return
        }

        val filePath = args[0]
        val jomadArgs = mutableListOf<Value>()
        for (arg in args.drop(1))
            jomadArgs.addLast(newString(arg))

        try {
            interpreter.getGlobalEnvironment().setBindingOrThrow("args", newList(jomadArgs))
            interpreter.doFileOrThrow(filePath)
        } catch (_: FileNotFoundException) {
            println("File not found: $filePath")
            exitProcess(1)
        } catch (e: EvaluationException) {
            println("Error: ${e.message}")
            e.printCallStack()
            exitProcess(1)
        } catch (e: Throwable) {
            println("Error: ${e.message}")
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

        try {
            IO.println(interpreter.doStringOrThrow(sourceCode))
        } catch (e: EvaluationException) {
            println("Error: ${e.message}")
            e.printCallStack()
        } catch (e: Exception) {
            println("Error: $e")
        }
    }
}
