package org.example.calculator

class Calculator(val log: Boolean = false) {
    fun calculate(expression: String, variables: Map<String, Double> = mapOf()): Double {
        val lexer = Lexer()
        val tokens = lexer.parse(expression)
        val parser = Parser()
        val root = parser.parse(tokens)
        val executor = Executor(variables, log)
        return executor.execute(root)
    }
}