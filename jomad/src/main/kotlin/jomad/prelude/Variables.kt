package jomad.prelude

import jomad.evaluation.evaluateOrThrow
import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newUnit

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
