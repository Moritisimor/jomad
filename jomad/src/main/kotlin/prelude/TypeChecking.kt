package prelude

import errors.EvaluationException
import evaluation.evaluate
import expressions.Expression
import values.Environment
import values.Value

fun registerTypeCheckingFunctions(env: Environment) {
    env.registerNative("isstr", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isstr expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getString().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isnum", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isnum expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getNumber().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isbool", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isbool expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getBoolean().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("islist", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("islist expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getList().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isrecord", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isrecord expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getRecord().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isnative", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isstr expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getNative().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isfun", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isfun expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getLambda().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("ismac", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("ismac expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getMacro().isSuccess)) },
            { Result.failure(it) }
        )
    })

    env.registerNative("isunit", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("isunit expects 1 argument"))

        return evaluate(args[0], env).fold(
            { Result.success(Value.ValBoolean(it.getUnit().isSuccess)) },
            { Result.failure(it) }
        )
    })
}
