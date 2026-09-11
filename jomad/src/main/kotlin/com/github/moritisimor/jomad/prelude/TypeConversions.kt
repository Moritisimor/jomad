package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluateToStringOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newNumber

fun registerTypeConversionFunctions(env: Environment) {
    env.registerNativeThrowing("string_to_num", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("string_to_num expects 1 argument")

        return newNumber(evaluateToStringOrThrow(args[0], env).toDouble())
    })
}
