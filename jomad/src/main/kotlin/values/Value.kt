package values

import expressions.Expression

sealed interface Value {
    data object ValUnit : Value
    data class ValString(val value: String) : Value
    data class ValNumber(val value: Double) : Value
    data class ValBoolean(val value: Boolean) : Value
    data class ValList(val value: List<Value>) : Value
    data class ValRecord(val value: HashMap<String, Value>) : Value

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
