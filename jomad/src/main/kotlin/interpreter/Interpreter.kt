package interpreter

import evaluation.evaluate
import expressions.Expression
import lexer.Lexer
import parser.Parser
import values.Environment
import values.Value
import java.io.File
import java.io.FileNotFoundException

class Interpreter {
    private val globalEnvironment = Environment()
    constructor(empty: Boolean = false) {
        if (!empty) {
            prelude.registerTypeCheckingFunctions(globalEnvironment)
            prelude.registerFunctionalFunctions(globalEnvironment)
            prelude.registerVariableFunctions(globalEnvironment)
            prelude.registerConditionals(globalEnvironment)
            prelude.registerArithmetics(globalEnvironment)
            prelude.registerIO(globalEnvironment)
        }
    }

    fun registerFunctions(func: (Environment) -> Unit) = func(globalEnvironment)
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
    fun registerNative(name: String, callback: (List<Expression>, Environment) -> Result<Value>) {
        globalEnvironment.registerNative(name, callback)
    }
}