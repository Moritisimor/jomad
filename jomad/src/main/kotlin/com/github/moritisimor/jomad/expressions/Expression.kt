@file:JvmName("Exprs")

package com.github.moritisimor.jomad.expressions

import com.github.moritisimor.jomad.exceptions.EvaluationException

@Suppress("Unused")
fun newListLit(vararg elems: Expression) = Expression.ListLiteral(elems.toList())

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

    fun getListLiteralOrThrow(): List<Expression> = this.getListLiteral().getOrThrow()

    fun getSymbol(): Result<String> = when (this) {
        is Symbol -> Result.success(this.name)
        else -> Result.failure(EvaluationException("Could not coerce expression to symbol"))
    }

    fun getSymbolOrThrow(): String = this.getSymbol().getOrThrow()

    fun getStringLiteral(): Result<String> = when (this) {
        is StringLiteral -> Result.success(this.elem)
        else -> Result.failure(EvaluationException("Could not coerce expression to string literal"))
    }

    @Suppress("Unused")
    fun getStringLiteralOrThrow(): String = this.getStringLiteral().getOrThrow()

    fun getNumberLiteral(): Result<Double> = when (this) {
        is NumberLiteral -> Result.success(this.value)
        else -> Result.failure(EvaluationException("Could not coerce expression to number literal"))
    }

    @Suppress("Unused")
    fun getNumberLiteralOrThrow(): Double = this.getNumberLiteral().getOrThrow()

    fun getBooleanLiteral(): Result<Boolean> = when (this) {
        is BooleanLiteral -> Result.success(this.value)
        else -> Result.failure(EvaluationException("Could not coerce expression to boolean literal"))
    }

    @Suppress("Unused")
    fun getBooleanLiteralOrThrow(): Boolean = this.getBooleanLiteral().getOrThrow()

    fun getUnitLiteral(): Result<Unit> = when (this) {
        is UnitLiteral -> Result.success(Unit)
        else -> Result.failure(EvaluationException("Could not coerce expression to unit literal"))
    }

    @Suppress("Unused")
    fun getUnitLiteralOrThrow(): Unit = this.getUnitLiteral().getOrThrow()

    fun toSource(): String = when (this) {
        is Symbol -> return this.name
        is StringLiteral -> return "\"${this.elem}\""
        is NumberLiteral ->
            if (this.value % 1 == 0.0)
                return this.value.toInt().toString()
            else
                return this.value.toString()

        is BooleanLiteral -> return this.value.toString()
        is UnitLiteral -> return "unit"
        is ListLiteral -> {
            val builder = StringBuilder()
            builder.append('(')
            for ((idx, elem) in this.elems.withIndex()) {
                builder.append(elem.toSource())
                if (idx != this.elems.size - 1)
                    builder.append(' ')
            }

            builder.append(')')
            return builder.toString()
        }
    }
}