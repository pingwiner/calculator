package org.example.calculator

sealed class Token(val position: Int) {
    class Number(position: Int, val value: Double) : Token(position) {
        override fun toString() = "Number($value)"
    }
    class Variable(position: Int, val name: String) : Token(position) {
        override fun toString() = "Variable($name)"
    }
    class Operator(position: Int, val opType: OpType) : Token(position) {
        override fun toString() = "Operator(${opType.value})"
    }
    class BracketLeft(position: Int) : Token(position) {
        override fun toString() = "("
    }
    class BracketRight(position: Int) : Token(position) {
        override fun toString() = ")"
    }
}

enum class OpType(val value: Char) {
    PLUS('+'),
    MINUS('-'),
    MUL('*'),
    DIV('/');

    companion object {
        fun fromChar(c: Char): OpType? {
            return entries.firstOrNull {it.value == c}
        }
    }
}

