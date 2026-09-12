package com.github.moritisimor.jomad.srcprelude

import com.github.moritisimor.jomad.interpreter.Interpreter

fun registerConditionalsSrc(interp: Interpreter) {
    interp.doStringOrThrow("(letfun not (a) (if a false true))")
    interp.doStringOrThrow("(letmac unless (cond yes no) if (not cond) yes no)")
    interp.doStringOrThrow("(letmac when (cond body) if cond body unit)")
    interp.doStringOrThrow("(letmac != (lhs rhs) not (= lhs rhs))")
}
