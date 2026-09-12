package com.github.moritisimor.jomad.interpreter

import com.github.moritisimor.jomad.evaluation.evaluate
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.lexer.Lexer
import com.github.moritisimor.jomad.parser.Parser
import com.github.moritisimor.jomad.prelude.registerArithmetics
import com.github.moritisimor.jomad.prelude.registerConditionals
import com.github.moritisimor.jomad.prelude.registerFunctionalFunctions
import com.github.moritisimor.jomad.prelude.registerIO
import com.github.moritisimor.jomad.prelude.registerInternalExceptionFunctions
import com.github.moritisimor.jomad.prelude.registerListFunctions
import com.github.moritisimor.jomad.prelude.registerLoopFunctions
import com.github.moritisimor.jomad.prelude.registerRecordFunctions
import com.github.moritisimor.jomad.prelude.registerStringFunctions
import com.github.moritisimor.jomad.prelude.registerTypeCheckingFunctions
import com.github.moritisimor.jomad.prelude.registerTypeConversionFunctions
import com.github.moritisimor.jomad.prelude.registerVariableFunctions
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import java.io.File
import java.io.FileNotFoundException

class Interpreter {
    private val globalEnvironment = Environment()
    constructor() : this(false)
    constructor(empty: Boolean) {
        if (!empty) {
            registerInternalExceptionFunctions(globalEnvironment)
            registerTypeConversionFunctions(globalEnvironment)
            registerTypeCheckingFunctions(globalEnvironment)
            registerFunctionalFunctions(globalEnvironment)
            registerVariableFunctions(globalEnvironment)
            registerRecordFunctions(globalEnvironment)
            registerStringFunctions(globalEnvironment)
            registerListFunctions(globalEnvironment)
            registerLoopFunctions(globalEnvironment)
            registerConditionals(globalEnvironment)
            registerArithmetics(globalEnvironment)
            registerIO(globalEnvironment)
        }
    }

    @Suppress("Unused")
    fun getGlobalEnvironment(): Environment = globalEnvironment

    @Suppress("Unused")
    fun getGlobalBindingOrNull(name: String): Value? = globalEnvironment.getBindingOrNull(name)

    @Suppress("Unused")
    fun getGlobalBindingOrThrow(name: String): Value = globalEnvironment.getBindingOrThrow(name)

    @Suppress("Unused")
    fun mutateGlobalBindingOrThrow(name: String, newValue: Value): Unit =
        globalEnvironment.mutateBindingOrThrow(name, newValue)

    fun doString(sourceCode: String): Result<Value> {
        val lexer = Lexer(sourceCode)
        val expressions = Parser(lexer.tokenize().fold(
            onSuccess = { it },
            onFailure = { return Result.failure(it) },
        )).parse()

        var lastValue: Value = Value.ValUnit
        for (expression in expressions) {
            evaluate(expression, globalEnvironment).fold(
                onSuccess = { lastValue = it },
                onFailure = { return Result.failure(it) }
            )
        }

        return Result.success(lastValue)
    }

    @Suppress("Unused")
    fun doStringOrThrow(sourceCode: String): Value = doString(sourceCode).fold(
        onSuccess = { it },
        onFailure = { throw it }
    )

    fun doFile(path: String): Result<Value> {
        try {
            val file = File(path)
            return doString(file.readText(Charsets.UTF_8))
        } catch (exn: FileNotFoundException) {
            return Result.failure(exn)
        }
    }

    @Suppress("Unused")
    fun doFileOrThrow(path: String): Value = doFile(path).fold(
        onSuccess = { it },
        onFailure = { throw it }
    )

    @Suppress("Unused")
    fun registerNative(name: String, callback: (List<Expression>, Environment) -> Result<Value>) =
        globalEnvironment.registerNative(name, callback)

    @Suppress("Unused")
    fun registerNativeThrowing(name: String, callback: (List<Expression>, Environment) -> Value) =
        globalEnvironment.registerNativeThrowing(name, callback)
}