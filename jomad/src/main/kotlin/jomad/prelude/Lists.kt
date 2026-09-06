package jomad.prelude

import jomad.errors.EvaluationException
import jomad.evaluation.evaluateOrThrow
import jomad.evaluation.evaluateToListOrThrow
import jomad.expressions.Expression
import jomad.values.Environment
import jomad.values.Value
import jomad.values.newList
import jomad.values.newUnit

fun registerListFunctions(env: Environment) {
    env.registerNativeThrowing("list", fun(args: List<Expression>, env: Environment): Value {
        val acc = mutableListOf<Value>()
        for (arg in args)
            acc.addLast(evaluateOrThrow(arg, env))

        return newList(acc)
    })

    env.registerNativeThrowing("car", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("car expects 1 argument")

        val l = evaluateToListOrThrow(args[0], env)
        if (l.isEmpty())
            return newUnit()

        return l[0]
    })

    env.registerNativeThrowing("cdr", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("cdr expects 1 argument")

        val l = evaluateToListOrThrow(args[0], env)
        if (l.isEmpty())
            return newUnit()

        return newList(l.subList(1, l.size))
    })

    env.registerNativeThrowing("cons", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("cons expects 2 arguments")

        val elem = evaluateOrThrow(args[0], env)
        val l1 = evaluateToListOrThrow(args[1], env)
        val l2 = l1.toMutableList()
        l2.addFirst(elem)

        return newList(l2)
    })

    env.registerNativeThrowing("push", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("push expects 2 arguments")

        val elem = evaluateOrThrow(args[0], env)
        val l1 = evaluateToListOrThrow(args[1], env)
        val l2 = l1.toMutableList()
        l2.addLast(elem)

        return newList(l2)
    })

    env.registerNativeThrowing("append", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("append expects 2 arguments")

        val l1 = evaluateToListOrThrow(args[0], env)
        val l2 = evaluateToListOrThrow(args[1], env)

        val l3 = l1.toMutableList()
        for (elem in l2)
            l3.add(elem)

        return newList(l3)
    })
}
