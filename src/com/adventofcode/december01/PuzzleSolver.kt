package com.adventofcode.december01

import com.adventofcode.PuzzleSolverAbstract
import com.adventofcode.mylambdas.splitByCondition

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

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


