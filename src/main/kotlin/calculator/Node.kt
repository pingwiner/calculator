package org.example.calculator

import org.example.calculator.OpType.DIV
import org.example.calculator.OpType.MINUS
import org.example.calculator.OpType.MUL
import org.example.calculator.OpType.PLUS

sealed class Node() {
    class ContainerNode(val nodes: MutableList<Node> = mutableListOf()) : Node()
    class ValueNode(val value: Token.Number) : Node()
    class VariableNode(val variable: Token.Variable) : Node()
    class OperatorNode(val operator: Token.Operator, val left: Node? = null, val right: Node? = null) : Node() {
        val priority: Int
            get() = PRIORITY[operator.opType] ?: 0
    }
}

private val PRIORITY = mapOf(
    PLUS to 0,
    MINUS to 0,
    MUL to 1,
    DIV to 1,
)

const val MAX_PRIORITY = 1