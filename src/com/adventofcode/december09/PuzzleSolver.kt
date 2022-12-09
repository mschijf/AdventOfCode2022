package com.adventofcode.december09

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return doSolve(2).toString()
    }

    override fun resultPartTwo(): String {
        return doSolve(10).toString()
    }

    private fun doSolve(numberOfKnots: Int): Int {
        val knots = MutableList(numberOfKnots) { Position(0,0) }
        val tailSet = mutableSetOf(knots.last())
        input.inputLines
            .map{ line -> List(line.substring(2).toInt()) { Move(line[0]) } }
            .flatten()
            .forEach { mv ->
                knots[0] = knots[0].move(mv)
                for (i in 1 until knots.size) {
                    if (knots[i-1].distanceTooFar(knots[i])) {
                        knots[i] = knots[i].follow(knots[i-1])
                    }
                }
                tailSet.add(knots.last())
            }
        return tailSet.count()
    }
}

//----------------------------------------------------------------------------------------------------------------------

data class Position (val x: Int, val y: Int) {
    fun move(mv:Move) = Position(this.x+mv.dirX, y+mv.dirY)
    fun distanceTooFar(other: Position) = (this.x - other.x).absoluteValue > 1 || (this.y - other.y).absoluteValue > 1
    fun follow(other: Position) = Position(x + (other.x - x).sign, y + (other.y - y).sign)
}

class Move(dir: Char) {
    val dirX: Int
    val dirY: Int
    init {
        when(dir) {
            'U' -> {dirX = 0; dirY = 1}
            'D' -> {dirX = 0; dirY = -1}
            'L' -> {dirX = -1; dirY = 0}
            'R' -> {dirX = 1; dirY = 0}
            else -> {dirX=0; dirY=0; println("IMPOSSIBLE!!")}
        }
    }
}
