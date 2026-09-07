package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.evaluation.evaluateOrThrow
import jomad.evaluation.evaluateToNumberOrThrow
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newNumber
import jomad.values.newString

fun registerArithmetics(environment: Environment) {
    environment.registerNativeThrowing("+", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("+ expects 2 arguments")

        val x = evaluateOrThrow(args[0], env)
        val y = evaluateOrThrow(args[1], env)

        return when (x) {
            is Value.ValNumber -> when (y) {
                is Value.ValNumber -> newNumber(x.value + y.value)
                else -> throw EvaluationException("Expected number after $x")
            }

            is Value.ValString -> when (y) {
                is Value.ValString -> newString(x.value + y.value)
                else -> throw EvaluationException("Expected string after $y")
            }

            else -> throw EvaluationException("Cannot apply + on these values: $x and $y")
        }
    })

    environment.registerNativeThrowing("-", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("- expects 2 arguments")

        val x = evaluateToNumberOrThrow(args[0], env)
        val y = evaluateToNumberOrThrow(args[1], env)
        return newNumber(x - y)
    })

    environment.registerNativeThrowing("*", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("* expects 2 arguments")

        val x = evaluateOrThrow(args[0], env)
        val y = evaluateOrThrow(args[1], env)

        return when (x) {
            is Value.ValNumber -> when (y) {
                is Value.ValNumber -> Value.ValNumber(x.value * y.value)
                is Value.ValString -> {
                    val builder = StringBuilder()
                    repeat(x.value.toInt()) {
                        builder.append(y.value)
                    }

                    newString(builder.toString())
                }

                else -> throw EvaluationException("Expected string or number after $x")
            }

            is Value.ValString -> when (y) {
                is Value.ValNumber -> {
                    val builder = StringBuilder()
                    repeat(y.value.toInt()) {
                        builder.append(x.value)
                    }

                    newString(builder.toString())
                }

                else -> throw EvaluationException("Expected number after $x")
            }

            else -> throw EvaluationException("Cannot apply * on these values: $x and $y")
        }
    })

    environment.registerNativeThrowing("/", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("/ expects 2 arguments")

        val x = evaluateToNumberOrThrow(args[0], env)
        val y = evaluateToNumberOrThrow(args[1], env)
        return newNumber(x / y)
    })

    environment.registerNativeThrowing("mod", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("mod expects 2 arguments")

        val x = evaluateToNumberOrThrow(args[0], env)
        val y = evaluateToNumberOrThrow(args[1], env)
        return newNumber(x / y)
    })
}
