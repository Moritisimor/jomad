package jomad.prelude

import jomad.errors.EvaluationException
import jomad.evaluation.evaluate
import jomad.expressions.Expression
import jomad.evaluation.evaluateToBoolean
import jomad.evaluation.evaluateToNumber
import jomad.values.Environment
import jomad.values.Value

fun registerConditionals(env: Environment) {
    env.registerNative("if", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 3)
            return Result.failure(EvaluationException("if expects 3 arguments"))

        val cond = evaluateToBoolean(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return if (cond) {
            evaluate(args[1], env)
        } else {
            evaluate(args[2], env)
        }
    })

    env.registerNative("=", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("= expects 2 arguments"))

        val x = evaluate(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val y = evaluate(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return Result.success(Value.ValBoolean(x == y))
    })

    env.registerNative(">", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("> expects 2 arguments"))

        val lhs = evaluateToNumber(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val rhs = evaluateToNumber(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return Result.success(Value.ValBoolean(lhs > rhs))
    })

    env.registerNative(">=", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException(">= expects 2 arguments"))

        val lhs = evaluateToNumber(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val rhs = evaluateToNumber(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return Result.success(Value.ValBoolean(lhs >= rhs))
    })

    env.registerNative("<", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("< expects 2 arguments"))

        val lhs = evaluateToNumber(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val rhs = evaluateToNumber(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return Result.success(Value.ValBoolean(lhs < rhs))
    })

    env.registerNative("<=", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(EvaluationException("<= expects 2 arguments"))

        val lhs = evaluateToNumber(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val rhs = evaluateToNumber(args[1], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return Result.success(Value.ValBoolean(lhs <= rhs))
    })
}
