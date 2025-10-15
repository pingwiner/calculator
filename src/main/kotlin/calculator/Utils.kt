package org.example.calculator

import org.example.calculator.Node.*
import kotlin.collections.forEach

private const val defaultIndent = "  "

fun printTokens(tokens: List<Token>) {
    val sb = StringBuilder()
    tokens.forEach {
        sb.append(it.toString())
        sb.append(" ")
    }
    println(sb.toString())
}


fun printNode(node: Node, indent: String = "") {
    when(node) {
        is OperatorNode -> {
            println(indent + node.operator.opType.value)
            node.left?.let {
                printNode(it, indent + defaultIndent)
            }
            node.right?.let {
                printNode(it, indent + defaultIndent)
            }
        }
        is ValueNode -> {
            println(indent + node.value.value)
        }
        is VariableNode -> {
            println(indent + node.variable.name)
        }
        is ContainerNode -> {
            println(indent + "Container")
            printNodes(node.nodes, indent + defaultIndent)
        }
    }
}

fun printNodes(nodes: List<Node>, indent: String = "") {
    nodes.forEach {
        printNode(it, indent)
    }
}
