package prelude

import errors.EvaluationException
import evaluation.evaluate
import expressions.Expression
import values.Environment
import values.Value

fun registerArithmetics(environment: Environment) {
    environment.registerNative("+", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("+ expects 2 arguments"))

        val x = evaluate(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val y = evaluate(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return when (x) {
            is Value.ValNumber -> when (y) {
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value + y.value))
                else -> Result.failure(EvaluationException("Expected number after $x"))
            }

            is Value.ValString -> when (y) {
                is Value.ValString -> Result.success(Value.ValString(x.value + y.value))
                else -> Result.failure(EvaluationException("Expected string after $y"))
            }

            else -> Result.failure(EvaluationException("Cannot apply + on these values: $x and $y"))
        }
    })

    environment.registerNative("-", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("- expects 2 arguments"))

        val x = evaluate(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val y = evaluate(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return when (x) {
            is Value.ValNumber -> when (y) {
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value - y.value))
                else -> Result.failure(EvaluationException("Expected number after $x"))
            }

            else -> Result.failure(EvaluationException("Cannot apply - on these values: $x and $y"))
        }
    })
}
