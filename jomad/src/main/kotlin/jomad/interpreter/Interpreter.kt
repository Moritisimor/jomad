package jomad.interpreter

import jomad.evaluation.evaluate
import jomad.expressions.Expression
import jomad.lexer.Lexer
import jomad.parser.Parser
import jomad.prelude.registerArithmetics
import jomad.prelude.registerConditionals
import jomad.prelude.registerFunctionalFunctions
import jomad.prelude.registerIO
import jomad.prelude.registerInternalExceptionFunctions
import jomad.prelude.registerListFunctions
import jomad.prelude.registerLoopFunctions
import jomad.prelude.registerStringFunctions
import jomad.prelude.registerTypeCheckingFunctions
import jomad.prelude.registerTypeConversionFunctions
import jomad.prelude.registerVariableFunctions
import jomad.values.Environment
import jomad.values.Value
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