package jomad.prelude

import jomad.evaluation.evaluateToStringOrThrow
import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newNumber

fun registerTypeConversionFunctions(env: Environment) {
    env.registerNativeThrowing("string_to_num", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("string_to_num expects 1 argument")

        return newNumber(evaluateToStringOrThrow(args[0], env).toDouble())
    })
}
