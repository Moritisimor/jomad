package com.github.moritisimor.jomad.srcprelude

import com.github.moritisimor.jomad.interpreter.Interpreter

fun registerArithmeticsSrc(interp: Interpreter) {
    interp.doStringOrThrow("(letfun inc (i) (+ i 1))")
    interp.doStringOrThrow("(letfun dec (i) (- i 1))")
}
