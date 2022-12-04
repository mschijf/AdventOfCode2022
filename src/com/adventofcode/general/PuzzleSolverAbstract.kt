package com.adventofcode.general

abstract class PuzzleSolverAbstract (
    private val test: Boolean){
    private val dayOfMonth = getDayOfMonthFromClassName(this)
    protected val input = Input(test, dayOfMonth)

    open fun resultPartOne(): String = "NOT IMPLEMENTED"
    open fun resultPartTwo(): String = "NOT IMPLEMENTED"

    fun showResult() {
        val output1 = resultPartOne()
        val output2 = resultPartTwo()

        println("Day          : $dayOfMonth")
        print("Version      : ")
        if (test) println("test input!!!") else println("real input")
        print("Input lines  : ${input.inputLines.count()}")
        if (input.inputLines.count() == 0) println("  POSSIBLE ERROR!! ") else println()
        println("---------------------------------")
        println("Result part 1: $output1")
        println("Result part 2: $output2")
    }
}