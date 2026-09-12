@file:JvmName("Eval")

package com.github.moritisimor.jomad.evaluation

import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value

fun evaluate(expr: Expression, env: Environment): Result<Value> = when (expr) {
    is Expression.BooleanLiteral -> Result.success(Value.ValBoolean(expr.value))
    is Expression.NumberLiteral -> Result.success(Value.ValNumber(expr.value))
    is Expression.StringLiteral -> Result.success(Value.ValString(expr.elem))
    is Expression.Symbol -> {
        try {
            val x = env.getBinding(expr.name)
            return Result.success(x.getOrThrow())
        } catch (e: EvaluationException) {
            return Result.failure(EvaluationException(e.message, e.callStack + expr))
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    is Expression.UnitLiteral -> Result.success(Value.ValUnit)
    is Expression.ListLiteral -> {
        val funList = expr.elems
        if (funList.isEmpty())
            return Result.success(Value.ValList(listOf()))

        val funExpr: Value
        try {
            funExpr = evaluateOrThrow(funList.first(), env)
        } catch (e: EvaluationException) {
            return Result.failure(EvaluationException(e.message, e.callStack + expr))
        } catch (e: Throwable) {
            return Result.failure(e)
        }

        when (funExpr) {
            is Value.ValNativeFunction -> {
                try {
                    val x = funExpr.callback(funList.subList(1, funList.size), env)
                    x.getOrThrow()  // We don't actually care about the result here,
                    return x        // we just want to throw if it's an error
                } catch (e: EvaluationException) {
                    return Result.failure(EvaluationException(e.message, e.callStack + expr))
                } catch (e: Throwable) {
                    return Result.failure(e)
                }
            }

            is Value.ValLambda -> {
                val funParams = funList.subList(1, funList.size)
                val paramAcc = mutableListOf<Value>()

                for (param in funParams) {
                    try {
                        paramAcc.addLast(evaluateOrThrow(param, env))
                    } catch (e: EvaluationException) {
                        return Result.failure(EvaluationException(e.message, e.callStack + expr))
                    } catch (e: Throwable) {
                        return Result.failure(e)
                    }
                }

                try {
                    val x = funExpr.invokeOrThrow(*paramAcc.toTypedArray())
                    return Result.success(x)
                } catch (e: EvaluationException) {
                    return Result.failure(EvaluationException(e.message, e.callStack + expr))
                } catch (e: Throwable) {
                    return Result.failure(e)
                }
            }

            is Value.ValMacro -> throw NotImplementedError("Macros are not yet implemented!")
            else -> Result.failure(EvaluationException(
                "Attempt to invoke non-callable object", listOf(expr)
            ))
        }
    }
}

@Suppress("Unused")
fun evaluateOrThrow(expr: Expression, env: Environment): Value = evaluate(expr, env).getOrThrow()
