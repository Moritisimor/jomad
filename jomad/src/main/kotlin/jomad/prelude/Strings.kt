package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.evaluation.evaluate
import jomad.evaluation.evaluateToStringOrThrow
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newList
import jomad.values.newNumber
import jomad.values.newString

fun registerStringFunctions(env: Environment) {
    env.registerNativeThrowing("splitws", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("splitws expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)

        val tempList = mutableListOf<Value>()
        for (part in str.split(" ").filter { it.isNotBlank() })
            tempList.add(Value.ValString(part))

        return newList(tempList)
    })

    env.registerNativeThrowing("strlen", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("strlen expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)
        return newNumber(str.length.toDouble())
    })

    env.registerNativeThrowing("sprint", fun(args: List<Expression>, env: Environment): Value {
        if (args.isEmpty())
            throw EvaluationException("sprint expects at least 1 argument")

        val builder = StringBuilder()
        for (arg in args) {
            evaluate(arg, env)
                .onSuccess { builder.append(it.toString()) }
                .onFailure { throw it }
        }

        return newString(builder.toString())
    })

    env.registerNativeThrowing("lower", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("lower expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)
        return newString(str.lowercase())
    })

    env.registerNativeThrowing("upper", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("upper expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)
        return newString(str.uppercase())
    })

    env.registerNativeThrowing("chars", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("chars expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)
        val tempList = mutableListOf<Value>()
        for (c in str.toCharArray())
            tempList.addLast(newString(c.toString()))

        return newList(tempList)
    })

    env.registerNativeThrowing("trim", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
           throw EvaluationException("trim expects 1 argument")

        val str = evaluateToStringOrThrow(args[0], env)
        return newString(str.trim())
    })
}
