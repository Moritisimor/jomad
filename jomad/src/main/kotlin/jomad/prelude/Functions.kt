package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value

fun registerFunctionalFunctions(env: Environment) {
    env.registerNative("letfun", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 3)
            return Result.failure(EvaluationException("letfun expects 3 arguments"))

        val funName = args[0].getSymbolOrThrow()
        val paramList = args[1].getListLiteralOrThrow()
        val funParams = mutableListOf<String>()

        for (param in paramList) {
            when (param) {
                is Expression.Symbol -> funParams.add(param.name)
                else -> return Result.failure(EvaluationException(
                    "Expected symbol expression in parameter list"
                ))
            }
        }

        val funBody = args[2]
        env.setBinding(funName, Value.ValLambda(
            parameters = funParams.toList(),
            body = funBody,
            captured = env
        )).onFailure { return Result.failure(it) }

        return Result.success(Value.ValUnit)
    })
}
