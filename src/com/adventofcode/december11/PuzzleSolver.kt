package com.adventofcode.december11

import com.adventofcode.PuzzleSolverAbstract
import com.adventofcode.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): String {
        val monkeyGroup = MonkeyGroup(input.inputLines, extraStress = false)
        println("${monkeyGroup.dividerProduct}")
        repeat(20) {
            monkeyGroup.doRound()
//            println()
//            println ("After round ${it+1} ")
//            monkeyGroup.print()
        }
        val activeMonkeyCount = monkeyGroup.monkeyList
            .map {it.inspectionCount}
            .sortedDescending()
        return (activeMonkeyCount[0] * activeMonkeyCount[1]).toString()
    }

    override fun resultPartTwo(): String {
        val monkeyGroup = MonkeyGroup(input.inputLines, extraStress = true)
        println("${monkeyGroup.dividerProduct}")
        repeat(10000) {
            monkeyGroup.doRound()
//            if ((it+1) % 1000 == 0 || it == 0 || it == 19) {
//                println()
//                println("After round ${it + 1}")
//                monkeyGroup.print()
//            }
        }
        val activeMonkeyCount = monkeyGroup.monkeyList
            .map {it.inspectionCount}
            .sortedDescending()
        return (activeMonkeyCount[0] * activeMonkeyCount[1]).toString()
    }
}

//----------------------------------------------------------------------------------------------------------------------

class MonkeyGroup(inputLines: List<String>, extraStress: Boolean) {
    val monkeyList = inputLines
            .splitByCondition {it.isBlank()}
            .map { Monkey(this, it, extraStress)}

    val dividerProduct = monkeyList
        .map { it.divisibleBy }
        .reduce { acc, i ->  acc * i }

    fun getMonkey(monkeyNumber: Int) = monkeyList[monkeyNumber]

    fun doRound() {
        monkeyList.forEach { monkey -> monkey.doTurn() }
    }

    fun print() {
        monkeyList.forEach { monkey -> monkey.print() }
    }
}

class Monkey(private val monkeyGroup: MonkeyGroup, inputLines: List<String>, private val extraStress: Boolean) {
    private val monkeyNumber = inputLines[0]
        .substringAfter("Monkey ")
        .substringBefore(":")
        .trim()
        .toInt()

    private val itemList = inputLines[1]
        .substringAfter("Starting items: ")
        .split(",")
        .map{it.trim().toLong()}
        .toMutableList()

    private val operationIsMultiply = inputLines[2]
        .substringAfter("Operation: new = old ")
        .first() == '*'

    private val secondOperandIsItem = inputLines[2]
        .substringAfter("Operation: new = old ")
        .substring("* ".length)
        .trim() == "old"

    private val secondOperandValue = inputLines[2]
        .substringAfter("Operation: new = old ")
        .substring("* ".length)
        .trim()
        .toLongOrNull() ?: 0L

    val divisibleBy = inputLines[3]
        .substringAfter("Test: divisible by ")
        .trim()
        .toLong()

    private val onTrueThrowToMonkey = inputLines[4]
        .substringAfter("If true: throw to monkey ")
        .trim()
        .toInt()
    private val onFalseThrowToMonkey = inputLines[5]
        .substringAfter("If false: throw to monkey ")
        .trim()
        .toInt()

    var inspectionCount = 0L

    fun doTurn() {
        while (itemList.isNotEmpty()) {
            inspectionCount++
            var item = itemList.removeAt(0)
            item = operation(item)
            if (!extraStress) {
                item /= 3
            }
            if (item % divisibleBy == 0L) {
                monkeyGroup.getMonkey(onTrueThrowToMonkey).catchItem(item)
            } else {
                monkeyGroup.getMonkey(onFalseThrowToMonkey).catchItem(item)
            }
        }
    }

    fun catchItem(item: Long) {
        if (extraStress) {
            itemList.add(item % monkeyGroup.dividerProduct)
        } else {
            itemList.add(item)
        }
    }

    private fun operation(item: Long): Long {
        return if (secondOperandIsItem) {
            if (operationIsMultiply) {
                item * item
            } else {
                item + item
            }
        } else {
            if (operationIsMultiply) {
                item * secondOperandValue
            } else {
                item + secondOperandValue
            }
        }
    }

    fun print() {
        println("Monkey $monkeyNumber: $inspectionCount     $itemList")
    }
}

