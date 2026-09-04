package values

import expressions.Expression

sealed interface Value {
    data object ValUnit : Value
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
    ) : Value

    data class ValNativeFunction(
        val callback: (List<Expression>, Environment) -> Result<Value>
    ) : Value

    data class ValMacro(
        val parameters: List<String>,
        val body: List<Expression>
    ) : Value
}

fun newNative(callback: (List<Expression>, Environment) -> Result<Value>) = Value.ValNativeFunction(callback)
