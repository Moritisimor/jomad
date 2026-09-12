package com.github.moritisimor.jomad.exceptions

import com.github.moritisimor.jomad.expressions.Expression

open class EvaluationException(
    override val message: String?,
    val callStack: List<Expression>
) : JomadException(message) {
    constructor(message: String?) : this(message, listOf())

    fun printCallStack() {
        for ((idx, call) in callStack.withIndex()) {
            println("\t$idx -> ${call.toSource()}")
        }
    }
}
