package com.adventofcode.december05

import com.adventofcode.general.Input
import com.adventofcode.general.getDayOfMonthFromClassName

class PuzzleSolution {
    fun getIsTestRun() = false
    fun getDayOfMonth() = getDayOfMonthFromClassName(this)
    fun getInputLineCount() = input.inputLines.count()
    private val input = Input(getIsTestRun(), getDayOfMonth())

    fun resultPartOne(): Int {
        return -1
    }

    fun resultPartTwo(): Int {
        return -2
    }
}