package prelude

import errors.EvaluationException
import expressions.Expression
import values.Environment
import values.Value

fun registerFunctionalFunctions(env: Environment) {
    env.registerNative("letfun", fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 3)
            return Result.failure(EvaluationException("letfun expects 3 arguments"))

        val funName = args[0].getSymbol().fold(
            { it },
            { return Result.failure(it) }
        )

        val paramList = args[1].getListLiteral().fold(
            { it },
            { return Result.failure(it) }
        )

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
