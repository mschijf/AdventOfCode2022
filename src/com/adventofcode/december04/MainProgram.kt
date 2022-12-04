package com.adventofcode.december04

fun main() {
    val puzzleSolution = PuzzleSolution()

    val output1 = puzzleSolution.resultPartOne()
    val output2 = puzzleSolution.resultPartTwo()

    println("Day          : ${puzzleSolution.getDayOfMonth()}")
    print("Version      : ")
    if (puzzleSolution.getIsTestRun()) println("test input!!!") else println("real input")
    print("Input lines  : ${puzzleSolution.getInputLineCount()}")
    if (puzzleSolution.getInputLineCount() == 0) println("  POSSIBLE ERROR!! ") else println()
    println("---------------------------------")
    println("Result part 1: $output1")
    println("Result part 2: $output2")
}