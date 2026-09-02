import lexer.Lexer

fun main() {
    val lexer = Lexer("(println (+ \"Hello, \" \"World!\")) # whats up\n(+ 10 -10)")
    lexer.tokenize()
        .onSuccess { tokens -> for (token in tokens) { println(token) }}
        .onFailure { println(it) }
}
