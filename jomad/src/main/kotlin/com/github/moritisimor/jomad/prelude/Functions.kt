package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluateOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newUnit

fun registerFunctionalFunctions(env: Environment) {
    env.registerNativeThrowing("letfun", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("letfun expects 3 arguments")

        val funName = args[0].getSymbolOrThrow()
        val paramList = args[1].getListLiteralOrThrow()
        val funParams = mutableListOf<String>()

        for (param in paramList)
            funParams.add(param.getSymbolOrThrow())

        val funBody = args[2]
        env.setBindingOrThrow(funName, Value.ValLambda(
            parameters = funParams.toList(),
            body = funBody,
            captured = env
        ))

        return newUnit()
    })

    env.registerNativeThrowing("lambda", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("lambda expects 2 arguments")

        val paramList = args[0].getListLiteralOrThrow()
        val body = args[1]
        val funParams = mutableListOf<String>()

        for (param in paramList)
            funParams.addLast(param.getSymbolOrThrow())

        return Value.ValLambda(
            parameters = funParams.toList(),
            body = body,
            captured = env
        )
    })

    env.registerNativeThrowing("do", fun(args: List<Expression>, env: Environment): Value {
        var lastExpression: Value = newUnit()
        for (arg in args) {
            lastExpression = evaluateOrThrow(arg, env)
        }

        return lastExpression
    })

    env.registerNativeThrowing("scoped", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("scoped expects 2 arguments")

        val bindingList = args[0].getListLiteralOrThrow()
        val body = args[1]
        val localEnv = Environment(env)

        for (elem in bindingList) {
            val bindingPair = elem.getListLiteralOrThrow()
            if (bindingPair.size != 2)
                throw EvaluationException("Bad syntax in binding-pair (expected 2 elements)")

            val bindingName = bindingPair[0].getSymbolOrThrow()
            val bindingValue = evaluateOrThrow(bindingPair[1], env)
            localEnv.setBindingOrThrow(bindingName, bindingValue)
        }

        return evaluateOrThrow(body, localEnv)
    })

    env.registerNativeThrowing("letmac", fun(args: List<Expression>, env: Environment): Value {
        if (args.size < 3)
            throw EvaluationException("letmac expects at least 3 arguments")

        val macroName = args[0].getSymbolOrThrow()
        val macroParams = args[1].getListLiteralOrThrow()
        val macroBody = args.drop(2)
        val paramAcc = mutableListOf<String>()
        for (param in macroParams)
            paramAcc.addLast(param.getSymbolOrThrow())

        env.setBindingOrThrow(macroName, Value.ValMacro(
            parameters = paramAcc.toList(),
            body = macroBody,
        ))

        return newUnit()
    })
}
