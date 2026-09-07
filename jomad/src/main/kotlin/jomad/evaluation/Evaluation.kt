@file:JvmName("Eval")
package jomad.evaluation

import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

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
                val body = funExpr.body
                val captured = funExpr.captured
                val paramNames = funExpr.parameters
                val suppliedParams = funList.subList(1, funList.size)
                val expected = paramNames.size
                val actual = suppliedParams.size

                val localEnv = Environment(captured)
                if (paramNames.size != suppliedParams.size)
                    return Result.failure(EvaluationException(
                        "Lambda was invoked with the wrong amount of arguments. Expected: $expected, got: $actual",
                    ))

                for ((idx, param) in suppliedParams.withIndex()) {
                    val evaluated = evaluate(param, env).fold(
                        onSuccess = { it },
                        onFailure = { return Result.failure(it) }
                    )

                    localEnv.setBinding(paramNames[idx], evaluated)
                        .onFailure { return Result.failure(it) }
                }

                return evaluate(body, localEnv)
            }

            is Value.ValMacro -> throw NotImplementedError("Macros are not yet implemented!")
            else -> Result.failure(EvaluationException("Attempt to invoke non-callable object"))
        }
    }
}

@Suppress("Unused")
fun evaluateOrThrow(expr: Expression, env: Environment): Value = evaluate(expr, env).getOrThrow()
