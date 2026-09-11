package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluate
import com.github.moritisimor.jomad.evaluation.evaluateOrThrow
import com.github.moritisimor.jomad.evaluation.evaluateToStringOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value

fun registerInternalExceptionFunctions(env: Environment) {
    env.registerNativeThrowing("try", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("try expects 2 arguments")

        return evaluate(args[0], env).fold(
            { it },
            { evaluateOrThrow(args[1], env) }
        )
    })

    env.registerNativeThrowing("throw", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("throw expects 1 argument")

        val throwMessage = evaluateToStringOrThrow(args[0], env)
        throw EvaluationException(throwMessage)
    })
}
