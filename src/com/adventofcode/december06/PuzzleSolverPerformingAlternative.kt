package com.adventofcode.december06

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolverPerformingAlternative(test=false).showResult()
}

class PuzzleSolverPerformingAlternative(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return performingAlternative(input.inputLines.first(), 4).toString()
    }

    override fun resultPartTwo(): String {
        return performingAlternative(input.inputLines.first(), 14).toString()
    }

    private fun performingAlternative(s: String, windowSize: Int): Int {
        var i=0
        while (i < s.length-(windowSize-1) ) {
            val j = firstIndexDoubleLetter(s, i, windowSize)
            if (j == -1)
                return (i + windowSize)
            i = (j+1)
        }
        return -1
    }

    private fun firstIndexDoubleLetter(s: String, start: Int, length: Int): Int {
        for (i in start until start+length-1)
            for (j in i+1 until start+length)
                if (s[i] == s[j])
                    return i
        return -1
    }
}

//----------------------------------------------------------------------------------------------------------------------
