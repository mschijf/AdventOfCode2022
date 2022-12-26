package com.adventofcode

abstract class PuzzleSolverAbstract (
    val test: Boolean) {
    private val dayOfMonth = getDayOfMonthFromSubClassName()
    protected val input = Input(test, dayOfMonth)

    open fun resultPartOne(): String = "NOT IMPLEMENTED"
    open fun resultPartTwo(): String = "NOT IMPLEMENTED"

    fun showResult() {
        println("Day          : $dayOfMonth")
        println("Version      : ${if (test) "test" else "real"} input")
        println("Input lines  : ${if (input.inputLines.isEmpty()) "NO INPUT!!" else input.inputLines.count()} ")
        println("---------------------------------")
        println("Result part 1: ${resultPartOne()}")
        println("Result part 2: ${resultPartTwo()}")
    }

    private fun getDayOfMonthFromSubClassName(): Int {
        val className = this.javaClass.name
        val monthName = "december"
        val i = className.indexOf(monthName)
        val j = className.lastIndexOf(".")
        val dayOfMonth = className.substring(i + monthName.length, j)
        return dayOfMonth.toInt()
    }
}