package com.adventofcode.december01

fun main() {
    MainProgram().run()
}

class MainProgram {
    private val test = false

    fun run() {
        val input = Input(test)

        val puzzleSolution = PuzzleSolution(input)
        val output1 = puzzleSolution.resultPartOne()
        val output2 = puzzleSolution.resultPartTwo()

        println("December ${input.getDayOfMonthFromClassName()}")
        println("  Puzzle output part 1: $output1")
        println("  Puzzle output part 2: $output2")
    }
}