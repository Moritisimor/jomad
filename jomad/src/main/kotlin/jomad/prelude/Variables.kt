package jomad.prelude

import jomad.evaluation.evaluate
import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

fun registerVariableFunctions(env: Environment) {
    env.registerNative("let", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("let expects 2 arguments"))

        val bindingName = args[0].getSymbol().fold(
            { it },
            { return Result.failure(it) }
        )

        val bindingValue = evaluate(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        env.setBinding(bindingName, bindingValue).onFailure { return Result.failure(it) }
        return Result.success(Value.ValUnit)
    })
}
