package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.evaluation.evaluate
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

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

    environment.registerNative("*", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("* expects 2 arguments"))

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
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value * y.value))
                is Value.ValString -> {
                    val builder = StringBuilder()
                    repeat(x.value.toInt()) {
                        builder.append(y.value)
                    }

                    Result.success(Value.ValString(builder.toString()))
                }

                else -> Result.failure(EvaluationException("Expected string or number after $x"))
            }

            is Value.ValString -> when (y) {
                is Value.ValNumber -> {
                    val builder = StringBuilder()
                    repeat(y.value.toInt()) {
                        builder.append(x.value)
                    }

                    Result.success(Value.ValString(builder.toString()))
                }

                else -> Result.failure(EvaluationException("Expected number after $x"))
            }

            else -> Result.failure(EvaluationException("Cannot apply * on these values: $x and $y"))
        }
    })

    environment.registerNative("/", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("/ expects 2 arguments"))

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
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value / y.value))
                else -> Result.failure(EvaluationException("Expected number after $x"))
            }

            else -> Result.failure(EvaluationException("Cannot apply / on these values: $x and $y"))
        }
    })

    environment.registerNative("mod", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("mod expects 2 arguments"))

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
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value % y.value))
                else -> Result.failure(EvaluationException("Expected number after $x"))
            }

            else -> Result.failure(EvaluationException("Cannot apply mod on these values: $x and $y"))
        }
    })
}
