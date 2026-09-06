package jomad.errors

open class JomadException(override val message: String?) : RuntimeException(message)
