package jomad.prelude

import jomad.evaluation.evaluate
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

fun registerIO(environment: Environment) {
    environment.registerNative("print", fun(args: List<Expression>, env: Environment): Result<Value> {
        for (arg in args) {
            evaluate(arg, env)
                .onSuccess { print(it) }
                .onFailure { return Result.failure(it) }
        }

        return Result.success(Value.ValUnit)
    })

    environment.registerNative(
        "println",
        fun(
            args: List<Expression>,
            env: Environment
        ): Result<Value> {
        for (arg in args) {
            evaluate(arg, env)
                .onSuccess { print(it) }
                .onFailure { return Result.failure(it) }
            }

            print("\n")
            return Result.success(Value.ValUnit)
        }
    )

    environment.registerNative("readln", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.isEmpty()) {
            return Result.success(Value.ValString(readln()))
        }

        for (arg in args) {
            evaluate(arg, env)
                .onSuccess { print(it) }
                .onFailure { return Result.failure(it) }
        }

        return Result.success(Value.ValString(readln()))
    })
}
