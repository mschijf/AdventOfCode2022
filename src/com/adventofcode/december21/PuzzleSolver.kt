package com.adventofcode.december21

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val monkeyMap = input.inputLines.map { Monkey(it) }.associate { it.name to it }
        monkeyMap.values.forEach { it.makeFunctional(monkeyMap) }
        return monkeyMap["root"]!!.yell().toString()
    }

    override fun resultPartTwo(): String {
        val monkeyMap = input.inputLines.map { Monkey(it) }.associate { it.name to it }
        monkeyMap.values.forEach { it.makeFunctional(monkeyMap) }

        monkeyMap["root"]!!.setOperator('-')
        monkeyMap["humn"]!!.clearNumber()
        monkeyMap["root"]!!.yell()
        monkeyMap["root"]!!.updateShouldBe()
        val result = monkeyMap["humn"]!!.shouldBe
        return result.toString()
    }

}

//----------------------------------------------------------------------------------------------------------------------

class Monkey(private val inputStr: String) {
    val name: String = inputStr.substringBefore(": ")

    var shouldBe: Long? = null
    private var number: Long? = null
    private var leftOperand: Monkey? = null
    private var rightOperand: Monkey? = null
    private var operator: Char? = null

    fun setOperator(operatorChar: Char) {
        number = null
        operator = operatorChar
    }

    fun clearNumber() {
        number = null //todo
        leftOperand = null
        rightOperand = null
        operator = null
    }


    fun makeFunctional(monkeyMap: Map<String, Monkey>) {
        number = inputStr.substringAfter(": ").toLongOrNull()
        if (number == null) {
            val leftStr = inputStr.substringAfter(": ").substringBefore(" ")
            val rightStr = inputStr.substringAfterLast(" ")
            leftOperand = monkeyMap[leftStr]
            rightOperand = monkeyMap[rightStr]
            operator = inputStr.substringAfter(": ").substringAfter(" ").first()
        }
    }

    fun yell(): Long? {
        if (leftOperand != null && rightOperand != null) {
            number = operation(leftOperand!!, operator!!, rightOperand!!)
        }
        return number
    }

    private fun operation(leftOperand: Monkey, operator: Char, rightOperand: Monkey) : Long? {
        val left = leftOperand.yell()
        val right = rightOperand.yell()

        if (left != null && right != null) {
            val result = when (operator) {
                '+' -> left + right
                '*' -> left * right
                '-' -> left - right
                '/' -> left / right
                else -> 999999
            }
            return result
        } else {
            return null
        }
    }


    fun updateShouldBe() {
        shouldBe = 0L
        determineShouldBeOfChild()
    }

    private fun determineShouldBeOfChild() {
        if (leftOperand == null && rightOperand == null) {
            return
        }
        if (this.shouldBe == null) {
            println("ERRORRRRR")
            return
        }

        if (leftOperand!!.number == null && rightOperand!!.number != null) {
            leftOperand!!.shouldBe = when (operator) {
                '*' -> shouldBe!! / rightOperand!!.number!!
                '/' -> shouldBe!! * rightOperand!!.number!!
                '-' -> shouldBe!! + rightOperand!!.number!!
                '+' -> shouldBe!! - rightOperand!!.number!!
                else -> 99999
            }
            leftOperand!!.determineShouldBeOfChild()
        } else if (leftOperand!!.number != null && rightOperand!!.number == null) {
            rightOperand!!.shouldBe = when (operator) {
                '*' -> shouldBe!! / leftOperand!!.number!!
                '/' -> leftOperand!!.number!! / shouldBe!!
                '-' -> leftOperand!!.number!! - shouldBe!!
                '+' -> shouldBe!! - leftOperand!!.number!!
                else -> 99999
            }
            rightOperand!!.determineShouldBeOfChild()
        } else {
            println("MMM, surprise....")
        }
    }


    fun print() {
        print("$name: ")
        if (number != null) {
            println("$number")
        } else {
            println("${leftOperand!!.name} $operator ${rightOperand!!.name}")
        }
    }
}


