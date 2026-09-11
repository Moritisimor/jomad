package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.evaluation.evaluateOrThrow
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.evaluation.evaluateToBooleanOrThrow
import com.github.moritisimor.jomad.evaluation.evaluateToNumberOrThrow
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newBoolean
import com.github.moritisimor.jomad.values.newUnit

fun registerConditionals(env: Environment) {
    env.registerNativeThrowing("if", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("if expects 3 arguments")

        return if (evaluateToBooleanOrThrow(args[0], env))
            evaluateOrThrow(args[1], env)
        else
            evaluateOrThrow(args[2], env)
    })

    env.registerNativeThrowing("switch", fun(args: List<Expression>, env: Environment): Value {
        if (args.size < 2)
            throw EvaluationException("switch expects at least 2 arguments")

        val scrutinee = evaluateOrThrow(args[0], env)
        for (arg in args.subList(1, args.size)) {
            val switchArm = arg.getListLiteralOrThrow()
            if (switchArm.size != 2)
                throw EvaluationException("Malformed switch-arm")

            val switchCase = switchArm[0]
            val switchValue = switchArm[1]

            if (switchCase is Expression.Symbol) {
                if (switchCase.name == "_") {
                    return evaluateOrThrow(switchValue, env)
                }
            }

            if (evaluateOrThrow(switchCase, env) == scrutinee) {
                return evaluateOrThrow(switchValue, env)
            }
        }

        return newUnit()
    })

    env.registerNativeThrowing("not", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("not expects 1 argument")

        return newBoolean(!evaluateToBooleanOrThrow(args[0], env))
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
