package com.adventofcode.december01

import com.adventofcode.general.Input
import com.adventofcode.general.getDayOfMonthFromClassName
import com.adventofcode.mylambdas.splitByCondition

class PuzzleSolution {
    fun getIsTestRun() = false
    fun getDayOfMonth() = getDayOfMonthFromClassName(this)
    fun getInputLineCount() = input.inputLines.count()
    private val input = Input(getIsTestRun(), getDayOfMonth())

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


