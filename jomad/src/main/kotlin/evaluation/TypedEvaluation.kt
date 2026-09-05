package evaluation

import expressions.Expression
import values.Environment
import values.Value

fun evaluateToString(value: Expression, env: Environment): Result<String> =
    evaluate(value, env).fold(
        { it.getString() },
        { Result.failure(it) }
    )

@Suppress("Unused")
fun evaluateToStringOrThrow(value: Expression, env: Environment): String =
    evaluateToString(value, env).fold(
        { it },
        { throw it }
    )

fun evaluateToNumber(value: Expression, env: Environment): Result<Double> =
    evaluate(value, env).fold(
        { it.getNumber() },
        { Result.failure(it) }
    )

@Suppress("Unused")
fun evaluateToNumberOrThrow(value: Expression, env: Environment): Double =
    evaluateToNumber(value, env).fold(
        { it },
        { throw it }
    )

fun evaluateToBoolean(value: Expression, env: Environment): Result<Boolean> =
    evaluate(value, env).fold(
        { it.getBoolean() },
        { Result.failure(it) }
    )

@Suppress("Unused")
fun evaluateToBooleanOrThrow(value: Expression, env: Environment): Boolean =
    evaluateToBoolean(value, env).fold(
        { it },
        { throw it }
    )

fun evaluateToList(value: Expression, env: Environment): Result<List<Value>> =
    evaluateToList(value, env).fold(
        { Result.success(it) },
        { Result.failure(it) }
    )

@Suppress("Unused")
fun evaluateToListOrThrow(value: Expression, env: Environment): List<Value> =
    evaluateToList(value, env).fold(
        { it },
        { throw it }
    )

fun evaluateToRecord(value: Expression, env: Environment): Result<HashMap<String, Value>> =
    evaluate(value, env).fold(
        { it.getRecord() },
        { Result.failure(it) }
    )

@Suppress("Unused")
fun evaluateToRecordOrThrow(value: Expression, env: Environment): HashMap<String, Value> =
    evaluateToRecord(value, env).fold(
        { it },
        { throw it }
    )
