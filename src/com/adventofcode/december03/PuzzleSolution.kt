package com.adventofcode.december03

import com.adventofcode.general.Input
import com.adventofcode.general.getDayOfMonthFromClassName

class PuzzleSolution {
    fun getIsTestRun() = false
    fun getDayOfMonth() = getDayOfMonthFromClassName(this)
    fun getInputLineCount() = input.inputLines.count()
    private val input = Input(getIsTestRun(), getDayOfMonth())

    fun resultPartOne(): Int {
        return input.inputLines
            .map { Pair(it.substring(0, it.length / 2), it.substring(it.length / 2, it.length)) }
            .map { appearsInBoth(it.first, it.second) }
            .sumOf { toValue(it) }
    }

    fun resultPartTwo(): Int {
        return input.inputLines
            .chunked(3)
            .map { appearsInBoth(appearsInBoth(it[0], it[1]), it[2]) }
            .sumOf { toValue(it) }
    }

    private fun appearsInBoth(first: String, second: String): String {
        return first.filter { second.indexOf(it) >= 0 }
    }

    private fun toValue (s: String) : Int {
        return if (s.lowercase() == s) {
            s[0] - 'a' + 1
        } else {
            s[0] - 'A' + 27
        }
    }

}
