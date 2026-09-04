package values

import errors.EvaluationException

class Environment {
    private var bindings = HashMap<String, Value>()
    private val parent: Environment? = null

    fun getBinding(name: String): Result<Value> = when (val v = bindings[name]) {
        is Value -> Result.success(v)
        null -> when (parent) {
            null -> Result.failure(EvaluationException("Binding $name not found"))
            else -> parent.getBinding(name)
        }
    }

    fun setBinding(name: String, value: Value): Result<Unit> = when (bindings[name]) {
        is Value -> Result.failure(EvaluationException("Binding $name already exists in this scope"))
        null -> {
            bindings[name] = value
            return Result.success(Unit)
        }
    }

    fun mutateBinding(name: String, value: Value): Result<Unit> = when (bindings[name]) {
        null -> when (parent) {
            null -> Result.failure(EvaluationException("Binding $name not found, it cannot be mutated"))
            else -> parent.mutateBinding(name, value)
        }

        is Value -> {
            bindings[name] = value
            return Result.success(Unit)
        }
    }
}
