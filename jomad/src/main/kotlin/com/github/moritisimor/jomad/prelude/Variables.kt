package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluateOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newUnit

fun registerVariableFunctions(env: Environment) {
    env.registerNativeThrowing("let", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("let expects 2 arguments")

        val bindingName = args[0].getSymbolOrThrow()
        val bindingValue = evaluateOrThrow(args[1], env)
        env.setBindingOrThrow(bindingName, bindingValue)
        return newUnit()
    })

    env.registerNativeThrowing("mut", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("mut expects 2 arguments")

        val bindingName = args[0].getSymbolOrThrow()
        val newBindingValue = evaluateOrThrow(args[1], env)
        env.mutateBindingOrThrow(bindingName, newBindingValue)

        return newUnit()
    })
}
