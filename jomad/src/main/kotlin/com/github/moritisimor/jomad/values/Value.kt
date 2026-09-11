@file:JvmName("Values")

package com.github.moritisimor.jomad.values

import com.github.moritisimor.jomad.evaluation.evaluate
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.exceptions.TypeAssertionException
import com.github.moritisimor.jomad.expressions.Expression

fun newString(s: String): Value.ValString = Value.ValString(s)
fun newNumber(d: Double): Value.ValNumber = Value.ValNumber(d)
fun newBoolean(b: Boolean): Value.ValBoolean = Value.ValBoolean(b)
fun newList(l: List<Value>): Value.ValList = Value.ValList(l)
fun newUnit() = Value.ValUnit

@Suppress("Unused")
fun newRecord(hm: HashMap<String, Value>) = Value.ValRecord(hm)

sealed interface Value {
    data object ValUnit : Value {
        override fun toString(): String = "<UNIT>"
    }

    data class ValString(val value: String) : Value {
        override fun toString(): String = value
    }

    data class ValNumber(val value: Double) : Value {
        override fun toString(): String =
            if (value % 1 == 0.0)
                value.toInt().toString()
            else
                value.toString()
    }

    data class ValBoolean(val value: Boolean) : Value {
        override fun toString(): String = value.toString()
    }

    data class ValList(val value: List<Value>) : Value {
        override fun toString(): String = value.toString()
    }

    data class ValRecord(val value: HashMap<String, Value>) : Value {
        override fun toString(): String = value.toString()
    }

    data class ValLambda(
        val parameters: List<String>,
        val captured: Environment,
        val body: Expression
    ) : Value {
        override fun toString(): String = "<LAMBDA>"
        fun paramsSize() = parameters.size

        fun invoke(vararg suppliedParams: Value): Result<Value> {
            val expected = parameters.size
            val actual = suppliedParams.size

            val localEnv = Environment(captured)
            if (expected != actual)
                return Result.failure(EvaluationException(
                    "Lambda was invoked with the wrong amount of arguments. Expected: $expected, got: $actual",
                ))

            for ((idx, param) in suppliedParams.withIndex())
                localEnv.setBinding(parameters[idx], param)

            return evaluate(body, localEnv)
        }

        fun invokeOrThrow(vararg suppliedParams: Value): Value = invoke(*suppliedParams).getOrThrow()
    }

    data class ValNativeFunction(
        val callback: (List<Expression>, Environment) -> Result<Value>
    ) : Value {
        override fun toString(): String = "<NATIVE FUNCTION>"
    }

    data class ValMacro(
        val parameters: List<String>,
        val body: List<Expression>
    ) : Value {
        override fun toString(): String = "<MACRO>"
    }

    fun getString(): Result<String> = when (this) {
        is ValString -> return Result.success(this.value)
        else -> return Result.failure(TypeAssertionException("Value could not be coerced to a string"))
    }

    @Suppress("Unused")
    fun getStringOrThrow(): String = this.getString().getOrThrow()

    fun getNumber(): Result<Double> = when (this) {
        is ValNumber -> Result.success(this.value)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a number"))
    }

    @Suppress("Unused")
    fun getNumberOrThrow(): Double = getNumber().getOrThrow()

    fun getBoolean(): Result<Boolean> = when (this) {
        is ValBoolean -> Result.success(this.value)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a boolean"))
    }

    @Suppress("Unused")
    fun getBooleanOrThrow(): Boolean = getBoolean().getOrThrow()

    fun getList(): Result<List<Value>> = when (this) {
        is ValList -> Result.success(this.value)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a list"))
    }

    @Suppress("Unused")
    fun getListOrThrow(): List<Value> = getList().getOrThrow()

    fun getRecord(): Result<HashMap<String, Value>> = when (this) {
        is ValRecord -> Result.success(this.value)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a record"))
    }

    @Suppress("Unused")
    fun getRecordOrThrow(): HashMap<String, Value> = getRecord().getOrThrow()

    fun getNative(): Result<(List<Expression>, env: Environment) -> Result<Value>> = when(this) {
        is ValNativeFunction -> Result.success(this.callback)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a native function"))
    }

    @Suppress("Unused")
    fun getNativeOrThrow(): (List<Expression>, env: Environment) -> Result<Value> = getNative().getOrThrow()

    fun getLambda(): Result<ValLambda> = when(this) {
        is ValLambda -> Result.success(this)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a lambda"))
    }

    @Suppress("Unused")
    fun getLambdaOrThrow(): ValLambda = getLambda().getOrThrow()

    fun getMacro(): Result<ValMacro> = when(this) {
        is ValMacro -> Result.success(this)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a macro"))
    }

    @Suppress("Unused")
    fun getMacroOrThrow(): ValMacro = getMacro().getOrThrow()

    fun getUnit(): Result<Unit> = when(this) {
        is ValUnit -> Result.success(Unit)
        else -> Result.failure(TypeAssertionException("Value could not be coerced to a unit"))
    }

    @Suppress("Unused")
    fun getUnitOrThrow(): Unit = getUnit().getOrThrow()
}
