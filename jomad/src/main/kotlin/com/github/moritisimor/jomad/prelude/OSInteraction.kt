package com.github.moritisimor.jomad.prelude

import com.github.moritisimor.jomad.evaluation.evaluateToStringOrThrow
import com.github.moritisimor.jomad.exceptions.EvaluationException
import com.github.moritisimor.jomad.expressions.Expression
import com.github.moritisimor.jomad.values.Environment
import com.github.moritisimor.jomad.values.Value
import com.github.moritisimor.jomad.values.newList
import com.github.moritisimor.jomad.values.newNumber
import com.github.moritisimor.jomad.values.newString
import com.github.moritisimor.jomad.values.newUnit
import java.io.File

fun registerOSInteractiveFunctions(env: Environment) {
    env.registerNativeThrowing("read_file", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("read_file expects 1 argument")

        val filePath = evaluateToStringOrThrow(args[0], env)
        try {
            val file = File(filePath)
            return newString(file.readText())
        } catch (e: Exception) {
            throw EvaluationException("Error while reading $filePath: ${e.message}")
        }
    })

    env.registerNativeThrowing("read_file_bytes", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("read_file_bytes expects 1 argument")

        val filePath = evaluateToStringOrThrow(args[0], env)
        try {
            val file = File(filePath)
            val acc = mutableListOf<Value>()
            for (b in file.readBytes())
                acc.addLast(newNumber(b.toDouble()))

            return newList(acc)
        } catch (e: Exception) {
            throw EvaluationException("Error while reading $filePath: ${e.message}")
        }
    })

    env.registerNativeThrowing("write_file", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 2)
            throw EvaluationException("write_file expects 2 arguments")

        val filePath = evaluateToStringOrThrow(args[0], env)
        val content = evaluateToStringOrThrow(args[1], env)
        try {
            val file = File(filePath)
            file.writeText(content)
            return newUnit()
        } catch (e: Exception) {
            throw EvaluationException("Error while writing to $filePath: ${e.message}")
        }
    })

    env.registerNativeThrowing("remove_file", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("remove_file expects 1 argument")

        val filePath = evaluateToStringOrThrow(args[0], env)
        try {
            val file = File(filePath)
            file.delete()
            return newUnit()
        } catch (e: Exception) {
            throw EvaluationException("Error while removing $filePath: ${e.message}")
        }
    })

    env.registerNativeThrowing("read_dir", fun(args: List<Expression>, env: Environment): Value {
        val dirPath = when (args.size) {
            0 -> "."
            1 -> evaluateToStringOrThrow(args[0], env)
            else -> throw EvaluationException("read_dir expects 0 or 1 arguments")
        }

        try {
            val dir = File(dirPath)
            val fsNodeAcc = mutableListOf<Value>()
            val fsNodes = dir.list()
            if (fsNodes != null)
                for (fsNode in fsNodes)
                    fsNodeAcc.addLast(newString(fsNode))

            return newList(fsNodeAcc)
        } catch (e: Exception) {
            throw EvaluationException("Error while reading $dirPath: ${e.message}")
        }
    })

    env.registerNativeThrowing("mkdir", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("mkdir expects 1 argument")

        val dirPath = evaluateToStringOrThrow(args[0], env)
        try {
            val dir = File(dirPath)
            dir.mkdir()
            return newUnit()
        } catch (e: Exception) {
            throw EvaluationException("Error while creating $dirPath: ${e.message}")
        }
    })

    env.registerNativeThrowing("remove_dir", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("remove_dir expects 1 argument")

        val dirPath = evaluateToStringOrThrow(args[0], env)
        try {
            val dir = File(dirPath)
            if (dir.list()?.size != 1)
                throw EvaluationException("Cannot remove non-empty directory")

            dir.delete()
            return newUnit()
        } catch (e: Exception) {
            throw EvaluationException("Error while removing $dirPath: ${e.message}")
        }
    })

    env.registerNativeThrowing("remove_dir_recurse", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("remove_dir_recurse expects 1 argument")

        val dirPath = evaluateToStringOrThrow(args[0], env)
        try {
            val dir = File(dirPath)
            dir.deleteRecursively()
            return newUnit()
        } catch (e: Exception) {
            throw EvaluationException("Error while recursively removing $dirPath: ${e.message}")
        }
    })

    env.registerNativeThrowing("cwd", fun(args: List<Expression>, env: Environment): Value {
        if (args.isNotEmpty())
            throw EvaluationException("cwd expects no arguments")

        when (val cwd = System.getProperty("user.dir")) {
            null -> throw EvaluationException("Could not get current working directory")
            else -> return newString(cwd)
        }
    })

    env.registerNativeThrowing("get_env", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("get_env expects 1 argument")

        val varName = evaluateToStringOrThrow(args[0], env)
        when (val envVar = System.getenv(varName)) {
            null -> throw EvaluationException("No such environment variable: $varName")
            else -> return newString(envVar)
        }
    })

    env.registerNativeThrowing("get_env_unit", fun(args: List<Expression>, env: Environment): Value {
        if (args.size != 1)
            throw EvaluationException("get_env_unit expects 1 argument")

        val varName = evaluateToStringOrThrow(args[0], env)
        return when (val envVar = System.getenv(varName)) {
            null -> newUnit()
            else -> newString(envVar)
        }
    })
}
