package com.adventofcode.december01

import com.adventofcode.mylambdas.splitByCondition

class PuzzleSolution(private val input: Input) {

    fun resultPartOne(): Int {
        return input.inputLines
            .splitByCondition { it.isBlank() }
            .map {it.sumOf { it.toInt() }}
            .max()
    }

    fun resultPartTwo(): Int {
        return input.inputLines
            .splitByCondition { it.isBlank()}
            .map {it.sumOf { it.toInt() }}
            .sortedDescending()
            .take(3)
            .sum()
    }
}


