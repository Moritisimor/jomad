package jomad.interpreter

import jomad.evaluation.evaluate
import jomad.expressions.Expression
import jomad.lexer.Lexer
import jomad.parser.Parser
import jomad.prelude.registerArithmetics
import jomad.prelude.registerConditionals
import jomad.prelude.registerFunctionalFunctions
import jomad.prelude.registerIO
import jomad.prelude.registerStringFunctions
import jomad.prelude.registerTypeCheckingFunctions
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
            registerStringFunctions(globalEnvironment)
            registerTypeCheckingFunctions(globalEnvironment)
            registerFunctionalFunctions(globalEnvironment)
            registerVariableFunctions(globalEnvironment)
            registerConditionals(globalEnvironment)
            registerArithmetics(globalEnvironment)
            registerIO(globalEnvironment)
        }
    }

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

    @Suppress("UNUSED")
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

    @Suppress("UNUSED")
    fun doFileOrThrow(path: String): Value = doFile(path).fold(
        onSuccess = { it },
        onFailure = { throw it }
    )

    @Suppress("UNUSED")
    fun registerNative(name: String, callback: (List<Expression>, Environment) -> Result<Value>) =
        globalEnvironment.registerNative(name, callback)

    @Suppress("UNUSED")
    fun registerNativeThrowing(name: String, callback: (List<Expression>, Environment) -> Value) =
        globalEnvironment.registerNativeThrowing(name, callback)
}