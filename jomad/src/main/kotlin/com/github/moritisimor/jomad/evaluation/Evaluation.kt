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
    is Expression.Symbol -> env.getBinding(expr.name)
    is Expression.UnitLiteral -> Result.success(Value.ValUnit)
    is Expression.ListLiteral -> {
        val funList = expr.elems
        if (funList.isEmpty())
            return Result.success(Value.ValList(listOf()))

        val funExpr = evaluate(funList.first(), env).fold(
            onSuccess = { it },
            onFailure = { return Result.failure(it) }
        )

        when (funExpr) {
            is Value.ValNativeFunction -> funExpr.callback(funList.subList(1, funList.size), env)
            is Value.ValLambda -> {
                val funParams = funList.subList(1, funList.size)
                val expected = funExpr.paramsSize()
                val actual = funParams.size

                if (funParams.size != funExpr.paramsSize())
                    return Result.failure(EvaluationException(
                        "Lambda invoked with wrong amount of args. Expected: $expected, got: $actual",
                    ))

                val paramAcc = mutableListOf<Value>()
                for (param in funList.subList(1, funList.size)) {
                    evaluate(param, env).fold(
                        onSuccess = { paramAcc.add(it) },
                        onFailure = { return Result.failure(it) }
                    )
                }

                funExpr.invoke(*paramAcc.toTypedArray())
            }

            is Value.ValMacro -> throw NotImplementedError("Macros are not yet implemented!")
            else -> Result.failure(EvaluationException("Attempt to invoke non-callable object"))
        }
    }
}

@Suppress("Unused")
fun evaluateOrThrow(expr: Expression, env: Environment): Value = evaluate(expr, env).getOrThrow()
