package org.example.calculator

class Lexer {
    private val tokens = mutableListOf<Token>()

    private var state = State.SPACE
    private var currentTokenValue = StringBuilder()
    private var currentTokenPosition = 0

    fun parse(text: String): List<Token> {
        tokens.clear()
        state = State.SPACE
        currentTokenValue.clear()
        currentTokenPosition = 0

        "$text ".forEachIndexed { index, char ->
            when(state) {
                State.SPACE -> space(index, char)
                State.VARIABLE -> variable(index, char)
                State.NUMBER -> number(index, char)
            }
        }
        return tokens
    }

    private fun space(index: Int, char: Char) {
        val opType = OpType.fromChar(char)

        if (char.isLetter()) {
            currentTokenValue.append(char)
            currentTokenPosition = index
            state = State.VARIABLE
        } else if (char.isDigit()) {
            currentTokenValue.append(char)
            currentTokenPosition = index
            state = State.NUMBER
        } else if (char.isWhitespace()) {
            // Do nothing
        } else if (char == '.') {
            throw IllegalArgumentException("Illegal character $char at position $index")
        } else if (char == '(') {
            tokens.add(
                Token.BracketLeft(index)
            )
        } else if (char == ')') {
            tokens.add(
                Token.BracketRight(index)
            )
        } else if (opType != null) {
            tokens.add(
                Token.Operator(index, opType)
            )
        } else {
            throw IllegalArgumentException("Illegal character $char at position $index")
        }
    }

    private fun variable(index: Int, char: Char) {
        if (char.isLetter() || char.isDigit()) {
            currentTokenValue.append(char)
        } else {
            tokens.add(
                Token.Variable(currentTokenPosition, currentTokenValue.toString())
            )
            currentTokenValue.clear()
            state = State.SPACE
            space(index, char)
        }
    }

    private fun number(index: Int, char: Char) {
        if (char.isDigit() || char == '.') {
            currentTokenValue.append(char)
        } else {
            try {
                val number = currentTokenValue.toString().toDouble()
                tokens.add(
                    Token.Number(currentTokenPosition, number)
                )
                currentTokenValue.clear()
                state = State.SPACE
                space(index, char)
            } catch(e: Exception) {
                throw IllegalArgumentException("Illegal numeric value at position $currentTokenPosition")
            }
        }
    }

    private enum class State {
        SPACE,
        NUMBER,
        VARIABLE
    }
}