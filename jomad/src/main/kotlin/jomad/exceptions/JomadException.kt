package jomad.exceptions

open class JomadException(override val message: String?) : RuntimeException(message)
