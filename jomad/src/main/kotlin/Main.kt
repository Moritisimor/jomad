import interpreter.Interpreter

fun main() {
    val interpreter = Interpreter()
    while (true) {
        try {
            print("Jomad REPL >> ")
            val sourceCode = readln()
            interpreter.doString(sourceCode)
                .onFailure { println("Error while evaluating: $it") }
                .onSuccess { println(it) }
        } catch (_: Exception) {
            println("Goodbye!")
            return
        }
    }
}
