package org.example

import org.example.calculator.Executor
import org.example.calculator.Lexer
import org.example.calculator.Parser

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    try {
        val lexer = Lexer()
        val tokens = lexer.parse("-2 + 3 * -(x + 2) / 4.7")
        val parser = Parser()
        val root = parser.parse(tokens)
        val executor = Executor(
            mapOf(
                "x" to 1.0
            ), true
        )
        val result = executor.execute(root)
        println("result: $result")
    } catch (e: Exception) {
        println(e.message)
    }
}