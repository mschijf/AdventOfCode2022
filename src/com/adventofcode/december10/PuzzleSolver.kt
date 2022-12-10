package com.adventofcode.december10

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        var valueXregister = 1
        var sum = 0
        val requestedCycleNumbers = listOf(20, 60, 100, 140, 180, 220)
        input.inputLines
            .map{Command(it)}
            .map{cmd -> List(cmd.cycleLength-1){Cycle(0)} + Cycle(cmd.adder)  }
            .flatten()
            .forEachIndexed { cycleNumber, cycle ->
                if (cycleNumber+1 in requestedCycleNumbers) {
                    sum += (cycleNumber+1) * valueXregister
                }
                valueXregister += cycle.adder
            }
        return sum.toString()
    }

    override fun resultPartTwo(): String {
        var valueXregister = 1
        input.inputLines
            .map{Command(it)}
            .map{cmd -> List(cmd.cycleLength-1){Cycle(0)} + Cycle(cmd.adder)  }
            .flatten()
            .forEachIndexed { cycleNumber, cycle ->
                drawPixel(cycleNumber, valueXregister)
                valueXregister += cycle.adder
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

class Command(cmd: String) {
    val cycleLength = if (cmd.startsWith("noop")) 1 else 2
    val adder = if (cmd.startsWith("addx")) cmd.substring("addx".length+1).toInt() else 0
}

data class Cycle(val adder: Int)
