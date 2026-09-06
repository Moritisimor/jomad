@file:JvmName("Eval")
package jomad.evaluation

import jomad.errors.EvaluationException
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
        if (funList.isEmpty()) {
            return Result.success(Value.ValList(listOf()))
        }

        val funExpr = evaluate(funList.first(), env).fold(
            onSuccess = { it },
            onFailure = { return Result.failure(it) }
        )

        when (funExpr) {
            is Value.ValNativeFunction -> funExpr.callback(funList.subList(1, funList.size), env)
            else -> Result.failure(EvaluationException("Attempt to invoke non-callable object"))
        }
    }
}

@Suppress("Unused")
fun evaluateOrThrow(expr: Expression, env: Environment): Value = evaluate(expr, env).getOrThrow()
