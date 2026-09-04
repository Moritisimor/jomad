import errors.EvaluationException
import evaluation.evaluate
import expressions.Expression
import lexer.Lexer
import parser.Parser
import values.Environment
import values.Value
import values.newNative
import kotlin.system.exitProcess

fun main() {
    val lexer = Lexer("(+ 10 20)")
    val tokens = lexer.tokenize().fold(
        onSuccess = { tokens -> tokens },
        onFailure = {
            println("Error while parsing tokens: ${it.message}")
            exitProcess(1)
        }
    )

    val parser = Parser(tokens)
    val expressions = parser.parse()
    val localEnv = Environment()
    localEnv.setBinding("+", newNative(fun(args: List<Expression>, env: Environment): Result<Value> {
        if (args.size != 2)
            return Result.failure(Exception("Expected 2 arguments"))

        val x = evaluate(args[0], env).fold(
            onSuccess = { it },
            onFailure = { return Result.failure(it) }
        )

        val y = evaluate(args[1], env).fold(
            onSuccess = { it },
            onFailure = { return Result.failure(it) }
        )

        return when (x) {
            is Value.ValNumber -> when (y) {
                is Value.ValNumber -> Result.success(Value.ValNumber(x.value + y.value))
                else -> Result.failure(EvaluationException("Expected number after after ${x.value}"))
            }

            is Value.ValString -> when (y) {
                is Value.ValString -> Result.success(Value.ValString(y.value + y.value))
                else -> Result.failure(EvaluationException("Expected string after ${x.value}"))
            }

            else -> Result.failure(EvaluationException(""))
        }
    }))

        for (expression in expressions) {
        evaluate(expression, localEnv)
            .onSuccess { println("$expression evaluates to: $it") }
            .onFailure { println("Error while evaluating expression '$expression': $it") }
    }
}
