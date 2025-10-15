package org.example.calculator

import org.example.calculator.Node.*

class Executor(val variables: Map<String, Double> = mapOf(), val logOperations: Boolean = false) {
    fun execute(node: Node): Double {
        return node.getValue()
    }

    fun Node.getValue(): Double {
        return when(this) {
            is ValueNode -> getValue()
            is ContainerNode -> throw IllegalStateException("Bad AST tree state. Can't execute.")
            is OperatorNode -> getValue()
            is VariableNode -> getValue()
        }
    }

    fun ValueNode.getValue(): Double {
        return value.value
    }

    fun VariableNode.getValue(): Double {
        return variables[variable.name] ?: throw IllegalArgumentException("Variable not defined: ${variable.name}")
    }

    fun OperatorNode.getValue(): Double {
        val leftValue = left?.getValue()
        if ((leftValue == null) && (operator.opType != OpType.MINUS) && (operator.opType != OpType.PLUS)) {
            throw IllegalArgumentException("Illegal operator ${operator.opType.value} at position ${operator.position}")
        }
        val rightValue = right?.getValue()
            ?: throw IllegalArgumentException("Illegal operator ${operator.opType.value} at position ${operator.position}")
        return executeOperation(operator, leftValue ?: 0.0, rightValue)
    }

    private fun executeOperation(operator: Token.Operator, left: Double, right: Double): Double {
        val result = when(operator.opType) {
            OpType.PLUS -> left + right
            OpType.MINUS -> left - right
            OpType.MUL -> left * right
            OpType.DIV -> if (right != 0.0) {
                left / right
            } else {
                throw IllegalArgumentException("Division by zero at position: ${operator.position}")
            }
        }
        if (logOperations) logOperation(operator, left, right, result)
        return result
    }

    private fun logOperation(operator: Token.Operator, left: Double, right: Double, result: Double) {
        val s = "$left ${operator.opType.value} $right = $result"
        println(s)
    }
}
