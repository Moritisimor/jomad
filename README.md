# Jomad
[Nomad Lisp](https://github.com/Moritisimor/nomad-lisp) for the JVM! Written in Kotlin.

## Getting started
Jomad is planned to be published to the central maven repositories later, but for now, you will need to use a local .jar-file.

### Cloning and building with Gradle
#### Linux/MacOS/BSD (Shell)
```bash
git clone https://github.com/Moritisimor/jomad
cd jomad/jomad
./gradlew shadowJar
java -jar build/libs/jomad-1.0-SNAPSHOT-all.jar
```

#### Windows (cmd.exe)
```bash
git clone https://github.com/Moritisimor/jomad
cd jomad/jomad
./gradlew.bat shadowJar
java -jar build/libs/jomad-1.0-SNAPSHOT-all.jar
```

## Embedding Examples (Java)
```java
import com.github.moritisimor.jomad.evaluation.Eval;
import com.github.moritisimor.jomad.evaluation.TypedEval;
import com.github.moritisimor.jomad.exceptions.EvaluationException;
import com.github.moritisimor.jomad.interpreter.Interpreter;
import com.github.moritisimor.jomad.values.Values;

void main() {
    var interpreter = new Interpreter();
    interpreter.registerNativeThrowing("hi_from_java", (_, _) -> {
        IO.println("Hello from Java!");
        return Values.newUnit();
    });

    interpreter.registerNativeThrowing("log", (args, env) -> {
        var currentDate = new Date();
        IO.print(currentDate + ": ");
        if (args.isEmpty()) {
            IO.println("No Message");
        } else {
            for (var arg : args) {
                IO.print(Eval.evaluateOrThrow(arg, env).toString());
                IO.print(' ');
            }

            IO.print("\n");
        }

        return Values.newUnit();
    });

    interpreter.registerNativeThrowing("sqrt", (args, env) -> {
        if (args.size() != 1)
            throw new EvaluationException("sqrt expects 1 argument");

        var x = TypedEval.evaluateToNumberOrThrow(args.getFirst(), env);
        return Values.newNumber(Math.sqrt(x));
    });

    interpreter.doStringOrThrow("(hi_from_java)");

    interpreter.doStringOrThrow("(log \"WARNING!\" \"This is a log!\")");
    interpreter.doStringOrThrow("(log)"); // Empty log

    var result = interpreter.doStringOrThrow("(+ 1 2)").getNumberOrThrow();
    IO.println(result);

    var squareRoot = interpreter.doStringOrThrow("(sqrt 81)").getNumberOrThrow();
    IO.println(squareRoot);
}
```
