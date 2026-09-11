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
}
