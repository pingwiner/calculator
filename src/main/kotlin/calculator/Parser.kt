package org.example.calculator

import org.example.calculator.Node.*

class Parser() {
    val stack = mutableListOf<ContainerNode>()

    fun parse(tokens: List<Token>): Node {
        stack.clear()
        stack.add(ContainerNode())

        tokens.forEach{ token ->
            when(token) {
                is Token.Number -> {
                    stack.last().nodes.add(ValueNode(token))
                }
                is Token.Variable -> {
                    stack.last().nodes.add(VariableNode(token))
                }
                is Token.BracketLeft -> {
                    stack.add(ContainerNode())
                }
                is Token.BracketRight -> {
                    if (stack.size < 2) throw IllegalArgumentException("Illegal ) at position ${token.position}")
                    val node = stack.removeLast()
                    stack.last().nodes.add(node)
                }
                is Token.Operator -> {
                    stack.last().nodes.add(OperatorNode(token))
                }
            }
        }
        if (stack.size > 1) throw IllegalArgumentException("Unbalanced parentheses in expression")
        val nodes = squash(removeUnary(stack.last().nodes))
        //printNodes(nodes)
        return nodes.last()
    }

    private fun removeUnary(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        var skip = false
        nodes.forEachIndexed { index, node ->
            if (skip) {
                skip = false
            } else {
                if ( ((index == 0) || (nodes[index - 1] is OperatorNode))
                    && (index < nodes.size - 1)
                    && (node is OperatorNode)
                    && ((node.operator.opType == OpType.MINUS) || (node.operator.opType == OpType.PLUS))
                    && (nodes[index + 1] !is OperatorNode)) {
                    val minusNode = OperatorNode(
                        node.operator,
                        null,
                        squashNode(nodes[index + 1])
                    )
                    result.add(minusNode)
                    skip = true
                } else {
                    result.add(node)
                }
            }
        }
        return result
    }

    fun squashNode(node: Node): Node {
        if (node !is ContainerNode) return node
        return squash(node.nodes).last()
    }

    fun squash(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        result.addAll(nodes)
        var priority = MAX_PRIORITY
        while(priority >= 0) {
            var i = 1
            while (i < result.size - 1) {
                val node = result[i]
                if ((node is OperatorNode) && (node.priority == priority)) {
                    val newNode = OperatorNode(node.operator, squashNode(result[i - 1]), squashNode(result[i + 1]))
                    result.removeAt(i - 1)
                    result.removeAt(i - 1)
                    result.removeAt(i - 1)
                    result.add(i - 1, newNode)
                } else {
                    if (node is ContainerNode) {
                        val newNode = squashNode(node)
                        result.removeAt(i)
                        result.add(i,newNode)
                    }
                    i++
                }
            }
            priority--
        }
        if (result.size > 1) throw IllegalArgumentException("Syntax error. Can't parse expression.")
        return result
    }
}