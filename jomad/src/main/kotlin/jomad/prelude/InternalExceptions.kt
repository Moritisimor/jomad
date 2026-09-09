package jomad.prelude

import jomad.evaluation.evaluate
import jomad.evaluation.evaluateOrThrow
import jomad.evaluation.evaluateToStringOrThrow
import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

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
