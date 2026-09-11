package jomad.prelude

import jomad.evaluation.evaluateOrThrow
import jomad.evaluation.evaluateToBooleanOrThrow
import jomad.evaluation.evaluateToLambdaOrThrow
import jomad.evaluation.evaluateToNumberOrThrow
import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newNumber
import jomad.values.newUnit

fun registerLoopFunctions(env: Environment) {
    env.registerNativeThrowing("while", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("while expects 2 arguments")

        val cond = args[0]
        val body = args[1]
        var lastExpr: Value = newUnit()

        while (evaluateToBooleanOrThrow(cond, env)) {
            lastExpr = evaluateOrThrow(body, env)
        }

        return lastExpr
    })

    env.registerNativeThrowing("from", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("from expects 3 arguments")

        val lower = evaluateToNumberOrThrow(args[0], env).toInt()
        val upper = evaluateToNumberOrThrow(args[1], env).toInt()
        val callback = evaluateToLambdaOrThrow(args[2], env)
        if (lower >= upper)
            throw EvaluationException("First argument to from must be larger than second argument")

        var lastVal: Value = newUnit()
        for (i in lower..upper) {
            lastVal = callback.invokeOrThrow(newNumber(i.toDouble()))
        }

        return lastVal
    })
}
