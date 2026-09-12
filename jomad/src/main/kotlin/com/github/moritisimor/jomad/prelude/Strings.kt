package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.evaluation.evaluate
import com.github.moritisimor.jomad.evaluation.evaluateToStringOrThrow
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newList
import com.github.moritisimor.jomad.values.newString

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
