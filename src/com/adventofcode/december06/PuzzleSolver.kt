package com.adventofcode.december06

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return firstWindowWithAllDifferentCharacters(4).toString()
    }

    override fun resultPartTwo(): String {
        return firstWindowWithAllDifferentCharacters(14).toString()
    }

    private fun firstWindowWithAllDifferentCharacters(windowSize: Int): Int {
        return input.inputLines
            .first()
            .windowed(windowSize)
            .indexOfFirst{allDifferent(it)}
            .plus(windowSize)
    }

    private fun allDifferent(s: String): Boolean {
        for (i in 0 until s.length-1)
            for (j in i+1 until s.length)
                if (s[i] == s[j])
                    return false
        return true
    }
}

//----------------------------------------------------------------------------------------------------------------------
