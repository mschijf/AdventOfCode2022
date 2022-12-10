package com.adventofcode.december10

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolverAlternative(test=false).showResult()
}

class PuzzleSolverAlternative(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val requestedCycleNumbers = listOf(20, 60, 100, 140, 180, 220)
        return input.inputLines
            .asSequence()
            .map{Command(it)}
            .map{cmd -> List(cmd.cycleLength-1){0} + cmd.adder  }
            .flatten()
            .runningReduce { sum, cycleAdder -> sum + cycleAdder }
            .map{ it + 1 }
            .withIndex()
            .filter { iv -> iv.index+2 in requestedCycleNumbers }
            .sumOf { iv -> (iv.index+2) * iv.value }
            .toString()
    }

    override fun resultPartTwo(): String {
        var valueXregister = 1
        input.inputLines
            .map{Command(it)}
            .map{cmd -> List(cmd.cycleLength-1){0} + cmd.adder  }
            .flatten()
            .forEachIndexed { cycleNumber, cycleAdder ->
                drawPixel(cycleNumber, valueXregister)
                valueXregister += cycleAdder
            }
        println()
        return "END"
    }

    private fun drawPixel(cycleNumber: Int, valueXregister: Int) {
        val pixelPosition = cycleNumber % 40
        if (pixelPosition % 40 == 0) {
            println()
        }
        if (pixelPosition in valueXregister - 1 .. valueXregister + 1) {
            print("#")
        } else {
            print(".")
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------