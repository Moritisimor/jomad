package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluateOrThrow
import com.github.moritisimor.jomad.evaluation.evaluateToRecordOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newRecord
import com.github.moritisimor.jomad.values.newUnit

fun registerRecordFunctions(env: Environment) {
    env.registerNativeThrowing("record", fun(args: List<Expression>, env: Environment): Value {
        val hm = HashMap<String, Value>()
        for (arg in args) {
            val recordField = arg.getListLiteralOrThrow()
            if (recordField.size != 2)
                throw EvaluationException("Record arm has bad syntax (expected 2 elements)")

            val fieldName = recordField[0].getSymbolOrThrow()
            val fieldValue = evaluateOrThrow(recordField[1], env)
            hm[fieldName] = fieldValue
        }

        return newRecord(hm)
    })

    env.registerNativeThrowing(".", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException(". expects 2 arguments")

        val record = evaluateToRecordOrThrow(args[0], env)
        val fieldName = args[1].getSymbolOrThrow()
        when (val v = record[fieldName]) {
            null -> throw EvaluationException("No such field in record: $fieldName")
            else -> return v
        }
    })

    env.registerNativeThrowing("record_mut", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 3)
            throw EvaluationException("record_mut expects 3 arguments")

        val record = evaluateToRecordOrThrow(args[0], env)
        val fieldName = args[1].getSymbolOrThrow()
        val newValue = evaluateOrThrow(args[2], env)
        when (record[fieldName]) {
            null -> throw EvaluationException("No such field in record: $fieldName")
            else -> record[fieldName] = newValue
        }

        return newUnit()
    })
}
