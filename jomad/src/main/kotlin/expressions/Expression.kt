package expressions

sealed interface Expression {
    data class ListLiteral(val elems: List<Expression>) : Expression
    data class Symbol(val name: String) : Expression
    data class StringLiteral(val elem: String) : Expression
    data class NumberLiteral(val value: Double) : Expression
    data class BooleanLiteral(val value: Boolean) : Expression
    data object UnitLiteral : Expression
}