package jomad.prelude

import jomad.exceptions.EvaluationException
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newUnit

fun registerFunctionalFunctions(env: Environment) {
    env.registerNativeThrowing("letfun", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("letfun expects 3 arguments")

        val funName = args[0].getSymbolOrThrow()
        val paramList = args[1].getListLiteralOrThrow()
        val funParams = mutableListOf<String>()

        for (param in paramList)
            funParams.add(param.getSymbolOrThrow())

        val funBody = args[2]
        env.setBindingOrThrow(funName, Value.ValLambda(
            parameters = funParams.toList(),
            body = funBody,
            captured = env
        ))

        return newUnit()
    })

    env.registerNativeThrowing("lambda", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("lambda expects 2 arguments")

        val paramList = args[0].getListLiteralOrThrow()
        val body = args[1]
        val funParams = mutableListOf<String>()

        for (param in paramList)
            funParams.addLast(param.getSymbolOrThrow())

        return Value.ValLambda(
            parameters = funParams.toList(),
            body = body,
            captured = env
        )
    })
}
