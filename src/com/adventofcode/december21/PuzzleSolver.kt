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
        var links = 0L
        var rechts = Long.MAX_VALUE

        while (links < rechts) {
            val mid = links + ((rechts - links) / 2)
            val result = startWithNumber(mid)
            println(format("%25d (%25d) %25d --> %25d", links, mid, rechts, result))
            if (result == 0L) {
                return mid.toString()
            }

            if (result > 0L) {
                links = mid + 1L
            } else {
                rechts = mid - 1L
            }
        }
        return "NIET GEVONDEN"
    }

    private fun startWithNumber(startNumber: Long): Long {
        val monkeyMap = input.inputLines.map { Monkey(it) }.associate { it.name to it }
        monkeyMap.values.forEach { it.makeFunctional(monkeyMap) }

        monkeyMap["root"]!!.setOperator('-')
        monkeyMap["humn"]!!.setNumber(startNumber)
        val result = monkeyMap["root"]!!.yell()
        return result
    }

}

//----------------------------------------------------------------------------------------------------------------------

class Monkey(private val inputStr: String) {
    val name: String = inputStr.substringBefore(": ")

    private var number: Long? = null
    private var leftOperand: Monkey? = null
    private var rightOperand: Monkey? = null
    private var operator: Char? = null

    fun setOperator(operatorChar: Char) {
        number = null
        operator = operatorChar
    }

    fun setNumber(overrideNumber: Long) {
        number = overrideNumber
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

    fun yell(): Long {
        if (number == null) {
            number = operation(leftOperand!!, operator!!, rightOperand!!)
        }
        return number!!
    }

    private fun operation(leftOperand: Monkey, operator: Char, rightOperand: Monkey) : Long {
        return when (operator) {
            '+' -> leftOperand.yell() + rightOperand.yell()
            '*' -> leftOperand.yell() * rightOperand.yell()
            '-' -> leftOperand.yell() - rightOperand.yell()
            '/' -> leftOperand.yell() / rightOperand.yell()
            else -> 999999
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


