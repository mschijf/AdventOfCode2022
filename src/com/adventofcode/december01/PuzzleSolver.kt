package com.adventofcode.december01

import com.adventofcode.general.PuzzleSolverAbstract
import com.adventofcode.mylambdas.splitByCondition

fun main() {
    PuzzleSolver().showResult()
}

class PuzzleSolver : PuzzleSolverAbstract() {
    override fun getIsTestRun() = false

    override fun resultPartOne(): String {
        return input.inputLines
            .splitByCondition { it.isBlank() }
            .maxOf { it -> it.sumOf { it.toInt() }}
            .toString()
    }

    override fun resultPartTwo(): String {
        return input.inputLines
            .splitByCondition { it.isBlank()}
            .map { it -> it.sumOf { it.toInt() }}
            .sortedDescending()
            .take(3)
            .sum()
            .toString()
    }
}


