package com.adventofcode.december11

import com.adventofcode.PuzzleSolverAbstract
import com.adventofcode.mylambdas.splitByCondition
import com.adventofcode.mylambdas.substringBetween

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return doSolve(repetitions = 20, extraStress = false).toString()
    }

    override fun resultPartTwo(): String {
        return doSolve(repetitions = 10_000, extraStress = true).toString()
    }

    private fun doSolve(repetitions: Int, extraStress: Boolean): Long {
        val monkeyGroup = MonkeyGroup(input.inputLines, extraStress = extraStress)
        repeat(repetitions) {
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
        return (activeMonkeyCount[0] * activeMonkeyCount[1])
    }
}

//----------------------------------------------------------------------------------------------------------------------

class MonkeyGroup(inputLines: List<String>, extraStress: Boolean) {
    val monkeyList = inputLines
            .splitByCondition {it.isBlank()}
            .map { Monkey(this, it, extraStress)}

    val productOfDividableBys = inputLines
        .filter { it.contains("Test: divisible by ") }
        .map { it.substringAfter("Test: divisible by ").trim().toLong() }
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
        .substringBetween("Monkey ",":").trim().toInt()
    private val itemList = inputLines[1]
        .substringAfter("Starting items: ").split(",").map{it.trim().toLong()}.toMutableList()
    private val operationChar = inputLines[2]
        .substringAfter("Operation: new = old ").first()
    private val secondOperandIsOld = inputLines[2]
        .substringAfter("Operation: new = old ").substring("* ".length).trim() == "old"
    private val secondOperandConstant = inputLines[2]
        .substringAfter("Operation: new = old ").substring("* ".length).trim().toLongOrNull() ?: 0L
    private val divisibleBy = inputLines[3]
        .substringAfter("Test: divisible by ").trim().toLong()
    private val onTrueThrowToMonkey = inputLines[4]
        .substringAfter("If true: throw to monkey ").trim().toInt()
    private val onFalseThrowToMonkey = inputLines[5]
        .substringAfter("If false: throw to monkey ").trim().toInt()

    var inspectionCount = 0L

    fun doTurn() {
        itemList.forEach {old ->
            val new = operation(old) / if (extraStress) 1 else 3
            val throwToMonkey = monkeyGroup.getMonkey(if (new % divisibleBy == 0L) onTrueThrowToMonkey else onFalseThrowToMonkey)
            throwToMonkey.catchItem(new)
        }
        inspectionCount += itemList.size
        itemList.clear()
    }

    private fun catchItem(item: Long) {
        if (extraStress) {
            itemList.add(item % monkeyGroup.productOfDividableBys)
        } else {
            itemList.add(item)
        }
    }

    private fun operation(item: Long): Long {
        return if (operationChar == '*') {
            item * if (secondOperandIsOld) item else secondOperandConstant
        } else {
            item + if (secondOperandIsOld) item else secondOperandConstant
        }
    }

    fun print() {
        println("Monkey $monkeyNumber: $inspectionCount     $itemList")
    }
}

