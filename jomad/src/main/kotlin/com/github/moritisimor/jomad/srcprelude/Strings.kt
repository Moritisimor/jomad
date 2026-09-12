package com.github.moritisimor.jomad.srcprelude

import com.github.moritisimor.jomad.interpreter.Interpreter

fun registerStringFunctionsSrc(interp: Interpreter) {
    interp.doStringOrThrow("(letfun has_prefix (s1 s2) (begins_with (chars s1) (chars s2)))")
    interp.doStringOrThrow("(letfun has_suffix (s1 s2) (ends_with (chars s1) (chars s2)))")
    interp.doStringOrThrow("(letfun strlen (s) (len (chars s)))")
}
