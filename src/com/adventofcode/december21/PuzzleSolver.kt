package com.adventofcode.december21

import com.adventofcode.PuzzleSolverAbstract
import java.lang.String.format

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val monkeyMap = input.inputLines.map { Monkey(it) }.associate { it.name to it }
        monkeyMap.values.forEach { it.makeFunctional(monkeyMap) }
        return monkeyMap["root"]!!.yell().toString()
    }

    //8967170973852742391 --> too high
    override fun resultPartTwo(): String {
        val monkeyMap = input.inputLines.map { Monkey(it) }.associate { it.name to it }
        monkeyMap.values.forEach { it.makeFunctional(monkeyMap) }

        monkeyMap["root"]!!.setOperator('-')
        monkeyMap["humn"]!!.setNumber("x")
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
    private var number: String? = null
    private var leftOperand: Monkey? = null
    private var rightOperand: Monkey? = null
    private var operator: Char? = null

    fun setOperator(operatorChar: Char) {
        number = null
        operator = operatorChar
    }

    fun setNumber(overrideNumber: String) {
        number = overrideNumber
        leftOperand = null
        rightOperand = null
        operator = null
    }


    fun makeFunctional(monkeyMap: Map<String, Monkey>) {
        val numberLong = inputStr.substringAfter(": ").toLongOrNull()
        if (numberLong == null) {
            number = null
            val leftStr = inputStr.substringAfter(": ").substringBefore(" ")
            val rightStr = inputStr.substringAfterLast(" ")
            leftOperand = monkeyMap[leftStr]
            rightOperand = monkeyMap[rightStr]
            operator = inputStr.substringAfter(": ").substringAfter(" ").first()
        } else {
            number = numberLong.toString()
        }
    }

    fun yell(): String {
        if (number == null) {
            number = operation(leftOperand!!, operator!!, rightOperand!!)
        }
        return number!!
    }

    private fun operation(leftOperand: Monkey, operator: Char, rightOperand: Monkey) : String {
        val left = leftOperand.yell()
        val right = rightOperand.yell()

        if (left.toLongOrNull() != null && right.toLongOrNull() != null) {
            val result = when (operator) {
                '+' -> left.toLong() + right.toLong()
                '*' -> left.toLong() * right.toLong()
                '-' -> left.toLong() - right.toLong()
                '/' -> left.toLong() / right.toLong()
                else -> 999999
            }
            return result.toString()
        } else if (left.toLongOrNull() != null) {
            return left.toLong().toString() + operator + "(" + right +  ")"
        } else {
            return "(" + left + ")" + operator + right.toLong().toString()
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

        if (leftOperand!!.number!!.toLongOrNull() == null && rightOperand!!.number!!.toLongOrNull() != null) {
            leftOperand!!.shouldBe = when (operator) {
                '*' -> shouldBe!! / rightOperand!!.number!!.toLong()
                '/' -> shouldBe!! * rightOperand!!.number!!.toLong()
                '-' -> shouldBe!! + rightOperand!!.number!!.toLong()
                '+' -> shouldBe!! - rightOperand!!.number!!.toLong()
                else -> 99999
            }
            leftOperand!!.determineShouldBeOfChild()
        } else if (leftOperand!!.number!!.toLongOrNull() != null && rightOperand!!.number!!.toLongOrNull() == null) {
            rightOperand!!.shouldBe = when (operator) {
                '*' -> shouldBe!! / leftOperand!!.number!!.toLong()
                '/' -> leftOperand!!.number!!.toLong() / shouldBe!!
                '-' -> leftOperand!!.number!!.toLong() - shouldBe!!
                '+' -> shouldBe!! - leftOperand!!.number!!.toLong()
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


