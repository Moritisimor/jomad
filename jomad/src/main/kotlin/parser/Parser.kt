package parser

import expressions.Expression
import lexer.Token

class Parser(private val tokens: List<Token>) {
    private var index = 0

    fun parse(): List<Expression> {
        val acc: MutableList<Expression> = mutableListOf()

        while (index < tokens.size) {
            when (val token = tokens[index]) {
                is Token.SymbolLiteral -> {
                    acc.addLast(Expression.Symbol(token.value))
                    index++
                }

                is Token.StringLiteral -> {
                    acc.addLast(Expression.StringLiteral(token.value))
                    index++
                }

                is Token.NumberLiteral -> {
                    acc.addLast(Expression.NumberLiteral(token.value))
                    index++
                }

                is Token.BooleanLiteral -> {
                    acc.addLast(Expression.BooleanLiteral(token.value))
                    index++
                }

                is Token.UnitLiteral -> {
                    acc.addLast(Expression.UnitLiteral)
                    index++
                }

                is Token.LeftParenthesis -> {
                    index++
                    acc.addLast(Expression.ListLiteral(parse()))
                }

                is Token.RightParenthesis -> {
                    index++
                    return acc
                }
            }
        }

        return acc
    }
}
