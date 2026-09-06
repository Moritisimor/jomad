# jomad
Nomad Lisp for the JVM! Written in Kotlin.

## Embedding Examples (Java)
```java
import jomad.evaluation.Eval;
import jomad.interpreter.Interpreter;
import jomad.values.Values;

void main() {
    var interpreter = new Interpreter(); // Instantiate an interpreter
    interpreter.registerNativeThrowing("hi_from_java", (_, _) -> {
        IO.println("Hello from Java!"); // Do Java Stuff
        return Values.newUnit(); // Return unit (similar to void/null)
    });

    interpreter.registerNativeThrowing("log", (args, env) -> {
        var currentDate = new Date();
        IO.print(currentDate + ": ");
        if (args.isEmpty())
            IO.println("No Message");
        else
            for (var arg : args)
                IO.print(Eval.evaluateOrThrow(arg, env).toString());
                // Evaluate each received expression.

        IO.print("\n");
        return Values.newUnit();
    });

    interpreter.doStringOrThrow("(log \"WARNING! \" \"This is a log!\")"); // Run some code
    interpreter.doStringOrThrow("(log)"); // Empty log
    interpreter.doStringOrThrow("(hi_from_java)");
    
    // This basically tries to cast an evaluated expression to a double, throwing if it's not possible
    var result = interpreter.doStringOrThrow("(+ 1 2)").getNumberOrThrow();
    IO.println(result);
}
```
