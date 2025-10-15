package org.example

import org.example.calculator.Calculator
import org.example.calculator.Executor
import org.example.calculator.Lexer
import org.example.calculator.Parser

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    try {
        val expression = "-2 + 3 * -(x + 2) / 4.7"
        val calculator = Calculator(true)
        val result = calculator.calculate(
            expression,
            mapOf(
                "x" to 3.14
            )
        )
        println("result: $result")
    } catch (e: Exception) {
        println(e.message)
    }
}