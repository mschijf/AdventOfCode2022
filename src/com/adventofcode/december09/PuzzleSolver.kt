package com.adventofcode.december09

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        var head = Position(0,0)
        var tail = Position(0,0)
        val tailSet = mutableSetOf<Position>(tail)
        input.inputLines
            .map{createMoveList(it)}
            .flatten()
            .forEach {mv ->
            head = head.move(mv)
            if (head.distanceTooFar(tail)) {
                tail = tail.follow(head, mv)
                tailSet.add(tail)
//                println(tail)
            }
        }
        return tailSet.count().toString()
    }

    override fun resultPartTwo(): String {
        var knots = MutableList(10) {Position(0,0) }
        val tailSet = mutableSetOf<Position>(knots.last())
        input.inputLines
            .map{createMoveList(it)}
            .flatten()
            .forEach {mv ->
                knots[0] = knots[0].move(mv)
                for (i in 1 until knots.size) {
                    if (knots[i-1].distanceTooFar(knots[i])) {
                        knots[i] = knots[i].follow(knots[i-1], mv)
                    } else {
                        break
                    }
                }
                tailSet.add(knots.last())
            }
        return tailSet.count().toString()
    }

    fun createMoveList(s: String): List<Move> {
        val result = mutableListOf<Move>()
        val split =s.split(" ")
        val stepSize = split[1].toInt()
        repeat(stepSize) {
            result.add(Move(split[0]))
        }
        return result
    }
}



//----------------------------------------------------------------------------------------------------------------------

data class Position (val x: Int, val y: Int) {
    fun move(mv:Move) = Position(this.x+mv.dirX, y+mv.dirY)
    fun distanceTooFar(other: Position) = (this.x - other.x).absoluteValue > 1 || (this.y - other.y).absoluteValue > 1

    fun follow(other: Position, mv: Move): Position {
        if (x == other.x)
            return Position(x, y  + if (y < other.y) 1 else if (y > other.y) -1 else 0)
        else if (y == other.y)
            return Position(x  + if (x < other.x) 1 else if (x > other.x) -1 else 0, y)
        else
            return Position(x + if (x < other.x) 1 else -1, y + if (y < other.y) 1 else -1)
    }
}

class Move(dir: String) {
    val dirX: Int
    val dirY: Int
    init {
        when(dir) {
            "U" -> {dirX = 0; dirY = 1}
            "D" -> {dirX = 0; dirY = -1}
            "L" -> {dirX = -1; dirY = 0}
            "R" -> {dirX = 1; dirY = 0}
            else -> {dirX=0; dirY=0; println("IMPOSSIBLE!!")}
        }
    }
}
