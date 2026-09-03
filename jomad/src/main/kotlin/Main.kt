import lexer.Lexer
import parser.Parser
import kotlin.system.exitProcess

fun main() {
    val lexer = Lexer("(println (+ \"Hello, \" \"World!\")) # whats up\n(+ 10 -10)")
    val tokens = lexer.tokenize().fold(
        onSuccess = { tokens -> tokens },
        onFailure = {
            println("Error while parsing tokens: ${it.message}")
            exitProcess(1)
        }
    )

    val parser = Parser(tokens)
    val expressions = parser.parse()
    expressions.forEach { expression -> println(expression) }
}
