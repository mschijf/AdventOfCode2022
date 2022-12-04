package com.adventofcode.december02

import com.adventofcode.general.Input

class PuzzleSolution(private val input: Input) {

    private val letter1ToTool = mapOf('A' to Tool.ROCK, 'B' to Tool.PAPER, 'C' to Tool.SCISSORS)
    private val letter2ToTool = mapOf('X' to Tool.ROCK, 'Y' to Tool.PAPER, 'Z' to Tool.SCISSORS)
    private val letter2ToResult = mapOf('X' to Result.LOSS, 'Y' to Result.DRAW, 'Z' to Result.WIN)

    private fun outCome(pair: Pair<Tool, Tool>): Result {
        return when (pair.second) {
            Tool.ROCK -> when (pair.first) {
                Tool.ROCK -> Result.DRAW
                Tool.PAPER -> Result.LOSS
                Tool.SCISSORS -> Result.WIN
            }
            Tool.PAPER -> when (pair.first) {
                Tool.ROCK -> Result.WIN
                Tool.PAPER -> Result.DRAW
                Tool.SCISSORS -> Result.LOSS
            }
            Tool.SCISSORS -> when (pair.first) {
                Tool.ROCK -> Result.LOSS
                Tool.PAPER -> Result.WIN
                Tool.SCISSORS -> Result.DRAW
            }
        }
    }

    private fun findTool (pair: Pair<Tool, Result>): Tool {
        return when (pair.second) {
            Result.LOSS -> when (pair.first) {
                Tool.ROCK -> Tool.SCISSORS
                Tool.PAPER -> Tool.ROCK
                Tool.SCISSORS -> Tool.PAPER
            }
            Result.WIN -> when (pair.first) {
                Tool.ROCK -> Tool.PAPER
                Tool.PAPER -> Tool.SCISSORS
                Tool.SCISSORS -> Tool.ROCK
            }
            Result.DRAW -> pair.first
        }
    }

    fun resultPartOne(): Int {
        return input.inputLines
            .map{Pair(it[0], it[2])}
            .map{Pair(letter1ToTool[it.first]!!, letter2ToTool[it.second]!! )}
            .map{outCome(it).value + it.second.value }
            .sum()
    }

    fun resultPartTwo(): Int {
        return input.inputLines
            .map{Pair(it[0], it[2])}
            .map{Pair(letter1ToTool[it.first]!!, letter2ToResult[it.second]!! )}
            .map{Pair(it.first, findTool(it) )}
            .map{outCome(it).value + it.second.value }
            .sum()
    }
}

enum class Tool(val value: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3)
}

enum class Result(val value: Int) {
    LOSS(0),
    WIN(6),
    DRAW(3)
}