package jomad.lexer

import jomad.exceptions.TokenizerException

class Lexer(sourceCode: String) {
    private val tokens = mutableListOf<Token>()
    private var index = 0
    private val charStream = sourceCode.toCharArray()

    private fun current(): Char = charStream[index]
    private fun peek(): Char = charStream[index + 1]
    private fun advance(): Char = charStream[index++]
    private fun isTerminator(): Boolean = listOf(' ', '\t', '\n', '(', ')', '"').contains(charStream[index])

    private fun countParens(tokens: List<Token>): Pair<Int, Int> {
        var left = 0
        var right = 0
        tokens.forEach { token -> when (token) {
            is Token.RightParenthesis -> right++
            is Token.LeftParenthesis -> left++
            else -> Unit
        }}

        return Pair(left, right)
    }

    private fun scanNumLit(): Result<Double> {
        val builder = StringBuilder()
        while (!isTerminator()) {
            builder.append(current())
            if (index == charStream.size - 1) {
                return Result.failure(TokenizerException(
                    "Number literal was never ended (Got to \"$builder\")"
                ))
            }

            advance()
        }

        return try {
            Result.success(builder.toString().toDouble())
        } catch (exn: NumberFormatException) {
            Result.failure(TokenizerException(exn.message))
        }
    }

    private fun scanStringLit(): Result<String> {
        val builder = StringBuilder()
        for (c in charStream.asList().subList(index, charStream.size - 1)) {
            advance()
            if (c == '"') return Result.success(builder.toString())
            builder.append(c)
        }

        return Result.failure(TokenizerException(
            "String literal was never ended (Got to \"$builder\")"
        ))
    }

    private fun scanSymbolLit(): Result<String> {
        val builder = StringBuilder()
        while (!isTerminator()) {
            if (index == charStream.size - 1) {
                return Result.failure(TokenizerException(
                    "Symbol literal was never ended (Got to \"$builder\")"
                ))
            }

            builder.append(current())
            advance()
        }

        return Result.success(builder.toString())
    }

    fun tokenize(): Result<List<Token>> {
        while (index != charStream.size) {
            when (current()) {
                ' ', '\t', '\n' -> advance()
                '(' -> {
                    tokens.addLast(Token.LeftParenthesis); advance()
                }

                ')' -> {
                    tokens.addLast(Token.RightParenthesis); advance()
                }

                '"' -> {
                    advance()
                    scanStringLit()
                        .onSuccess { value -> tokens.addLast(Token.StringLiteral(value)) }
                        .onFailure { exn -> return Result.failure(TokenizerException(exn.message)) }
                }

                '#' -> {
                    while (current() != '\n') {
                        if (index == charStream.size - 1) break
                        advance()
                    }
                }

                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> scanNumLit()
                    .onSuccess { value -> tokens.addLast(Token.NumberLiteral(value)) }
                    .onFailure { exn -> return Result.failure(TokenizerException(exn.message)) }

                '-' -> {
                    if (listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9').contains(peek())) {
                        scanNumLit()
                            .onSuccess { value -> tokens.addLast(Token.NumberLiteral(value)) }
                            .onFailure { exn -> return Result.failure(TokenizerException(exn.message)) }
                    } else {
                        scanSymbolLit()
                            .onSuccess { value -> tokens.addLast(Token.SymbolLiteral(value)) }
                            .onFailure { exn -> return Result.failure(TokenizerException(exn.message)) }
                    }
                }

                else -> scanSymbolLit()
                    .onSuccess { value ->
                        when (value) {
                            "true" -> tokens.addLast(Token.BooleanLiteral(true))
                            "false" -> tokens.addLast(Token.BooleanLiteral(false))
                            "unit" -> tokens.addLast(Token.UnitLiteral)
                            else -> tokens.addLast(Token.SymbolLiteral(value))
                        }
                    }
                    .onFailure { exn -> return Result.failure(TokenizerException(exn.message)) }
            }
        }

        val parenCount = countParens(tokens)
        val left = parenCount.first
        val right = parenCount.second

        if (left > right)
            return Result.failure(TokenizerException("One or more unclosed left parenthesises"))

        if (right > left)
            return Result.failure(TokenizerException("One or more superfluous right parenthesises"))

        return Result.success(tokens)
    }
}
