package jomad.prelude

import jomad.errors.EvaluationException
import jomad.evaluation.evaluate
import jomad.evaluation.evaluateToString
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newError
import jomad.values.newValue

fun registerStringFunctions(env: Environment) {
    env.registerNative("splitws", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("splitws expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val tempList = mutableListOf<Value>()
        for (part in str.split(" ").filter { it.isNotBlank() })
            tempList.add(Value.ValString(part))

        return newValue(Value.ValList(tempList))
    })

    env.registerNative("strlen", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("strlen expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return newValue(Value.ValNumber(str.length.toDouble()))
    })

    env.registerNative("sprint", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.isEmpty())
            return Result.failure(EvaluationException("sprint expects at least 1 argument"))

        val builder = StringBuilder()
        for (arg in args) {
            evaluate(arg, env)
                .onSuccess { builder.append(it.toString()) }
                .onFailure { return newError(it) }
        }

        return newValue(Value.ValString(builder.toString()))
    })

    env.registerNative("lower", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("lower expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return newValue(Value.ValString(str.lowercase()))
    })

    env.registerNative("upper", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("upper expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return newValue(Value.ValString(str.uppercase()))
    })

    env.registerNative("chars", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("chars expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        val tempList = mutableListOf<Value>()
        for (c in str.toCharArray())
            tempList.addLast(Value.ValString(c.toString()))

        return newValue(Value.ValList(tempList))
    })

    env.registerNative("trim", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 1)
            return Result.failure(EvaluationException("trim expects 1 argument"))

        val str = evaluateToString(args[0], env).fold(
            { it },
            { return Result.failure(it) }
        )

        return newValue(Value.ValString(str.trim()))
    })
}
