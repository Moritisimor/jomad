@file:JvmName("Exprs")
package jomad.expressions

import jomad.errors.EvaluationException

@Suppress("Unused")
fun newListLit(elems: List<Expression>) = Expression.ListLiteral(elems)

@Suppress("Unused")
fun newSymbol(name: String) = Expression.Symbol(name)

@Suppress("Unused")
fun newStringLit(elem: String) = Expression.StringLiteral(elem)

@Suppress("Unused")
fun newNumLit(elem: Double) = Expression.NumberLiteral(elem)

@Suppress("Unused")
fun newBooleanLit(elem: Boolean) = Expression.BooleanLiteral(elem)

@Suppress("Unused")
fun newUnitLit() = Expression.UnitLiteral

sealed interface Expression {
    data class ListLiteral(val elems: List<Expression>) : Expression
    data class Symbol(val name: String) : Expression
    data class StringLiteral(val elem: String) : Expression
    data class NumberLiteral(val value: Double) : Expression
    data class BooleanLiteral(val value: Boolean) : Expression
    data object UnitLiteral : Expression

    fun getListLiteral(): Result<List<Expression>> = when (this) {
        is ListLiteral -> Result.success(this.elems)
        else -> Result.failure(EvaluationException("Could not coerce expression to list literal"))
    }

    @Suppress("UNUSED")
    fun getListLiteralOrThrow(): List<Expression> = this.getListLiteral().getOrThrow()

    fun getSymbol(): Result<String> = when (this) {
        is Symbol -> Result.success(this.name)
        else -> Result.failure(EvaluationException("Could not coerce expression to symbol"))
    }

    @Suppress("UNUSED")
    fun getSymbolOrThrow(): String = this.getSymbol().getOrThrow()

    fun getStringLiteral(): Result<String> = when (this) {
        is StringLiteral -> Result.success(this.elem)
        else -> Result.failure(EvaluationException("Could not coerce expression to string literal"))
    }

    @Suppress("UNUSED")
    fun getStringLiteralOrThrow(): String = this.getStringLiteral().getOrThrow()

    fun getNumberLiteral(): Result<Double> = when (this) {
        is NumberLiteral -> Result.success(this.value)
        else -> Result.failure(EvaluationException("Could not coerce expression to number literal"))
    }

    @Suppress("UNUSED")
    fun getNumberLiteralOrThrow(): Double = this.getNumberLiteral().getOrThrow()

    fun getBooleanLiteral(): Result<Boolean> = when (this) {
        is BooleanLiteral -> Result.success(this.value)
        else -> Result.failure(EvaluationException("Could not coerce expression to boolean literal"))
    }

    @Suppress("UNUSED")
    fun getBooleanLiteralOrThrow(): Boolean = this.getBooleanLiteral().getOrThrow()

    fun getUnitLiteral(): Result<Unit> = when (this) {
        is UnitLiteral -> Result.success(Unit)
        else -> Result.failure(EvaluationException("Could not coerce expression to unit literal"))
    }

    @Suppress("UNUSED")
    fun getUnitLiteralOrThrow(): Unit = this.getUnitLiteral().getOrThrow()
}