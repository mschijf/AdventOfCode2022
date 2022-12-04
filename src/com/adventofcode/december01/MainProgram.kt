package com.adventofcode.december01

import com.adventofcode.general.Input
import com.adventofcode.general.getDayOfMonthFromClassName

fun main() {
    MainProgram().run()
}

class MainProgram {
    private val test = false

    fun run() {
        val dayOfMonth = getDayOfMonthFromClassName(this)
        val input = Input(test, dayOfMonth)

        val puzzleSolution = PuzzleSolution(input)
        val output1 = puzzleSolution.resultPartOne()
        val output2 = puzzleSolution.resultPartTwo()

        println("December $dayOfMonth")
        println("  Puzzle output part 1: $output1")
        println("  Puzzle output part 2: $output2")
    }
}