package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.evaluation.evaluateOrThrow
import jomad.expressions.Expression
import jomad.evaluation.evaluateToBooleanOrThrow
import jomad.evaluation.evaluateToNumberOrThrow
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newBoolean

fun registerConditionals(env: Environment) {
    env.registerNativeThrowing("if", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("if expects 3 arguments")

        return if (evaluateToBooleanOrThrow(args[0], env))
            evaluateOrThrow(args[1], env)
        else
            evaluateOrThrow(args[2], env)
    })

    env.registerNativeThrowing("=", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("= expects 2 arguments")

        val x = evaluateOrThrow(args[0], env)
        val y = evaluateOrThrow(args[1], env)
        return newBoolean(x == y)
    })

    env.registerNativeThrowing(">", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("> expects 2 arguments")

        val lhs = evaluateToNumberOrThrow(args[0], env)
        val rhs = evaluateToNumberOrThrow(args[1], env)

        return newBoolean(lhs > rhs)
    })

    env.registerNativeThrowing(">=", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException(">= expects 2 arguments")

        val lhs = evaluateToNumberOrThrow(args[0], env)
        val rhs = evaluateToNumberOrThrow(args[1], env)

        return newBoolean(lhs >= rhs)
    })

    env.registerNativeThrowing("<", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("< expects 2 arguments")

        val lhs = evaluateToNumberOrThrow(args[0], env)
        val rhs = evaluateToNumberOrThrow(args[1], env)

        return newBoolean(lhs < rhs)
    })

    env.registerNativeThrowing("<=", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("<= expects 2 arguments")

        val lhs = evaluateToNumberOrThrow(args[0], env)
        val rhs = evaluateToNumberOrThrow(args[1], env)

        return newBoolean(lhs <= rhs)
    })
}
