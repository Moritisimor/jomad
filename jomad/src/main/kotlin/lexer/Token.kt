package lexer

sealed interface Token {
    data class NumberLiteral(val value: Int) : Token
    data class StringLiteral(val value: String) : Token
    data class BooleanLiteral(val value: Boolean) : Token
    data class SymbolLiteral(val value: String) : Token
    data object UnitLiteral : Token
    data object LeftParenthesis : Token
    data object RightParenthesis : Token
}
