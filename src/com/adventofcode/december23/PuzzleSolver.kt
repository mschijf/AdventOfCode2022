package com.adventofcode.december23

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val board = Board(input.inputLines, 10)
        repeat (board.maxRound)  { roundNumber ->
            val proposals = board.getProposedMoves(roundNumber)
            board.moveToProposals(proposals)
        }
        return board.countEmptyGroundTiles().toString()
    }

    override fun resultPartTwo(): String {
        val board = Board(input.inputLines, 1000)
        repeat (board.maxRound)  {roundNumber ->
            val proposals = board.getProposedMoves(roundNumber)
            if (proposals.isEmpty()) {
                return (roundNumber+1).toString()
            }
            board.moveToProposals(proposals)
        }
        return "No answer after ${board.maxRound}"
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Board(inputLines: List<String>, val maxRound: Int) {
    private val extraSpace = maxRound+1
    private val width = inputLines.maxOf { it.length }

    private val board = List(extraSpace) {CharArray(width + 2*extraSpace){ '.' } } +
                inputLines
                    .map { str -> CharArray(width + 2*extraSpace) { i -> if (i >= extraSpace && i < width+extraSpace) str[i-extraSpace] else '.' } } +
                List(extraSpace) {CharArray(width + 2*extraSpace){ '.' } }

    fun countEmptyGroundTiles(): Int {
        val mostLeftElf = board.filter{it.contains('#')}.minOf{row -> row.indexOfFirst {it == '#'}}
        val mostRightElf = board.filter{it.contains('#')}.maxOf{row -> row.indexOfLast {it == '#'}}
        val mostTopElf = board.indexOfFirst { it.contains('#') }
        val mostBottomElf = board.indexOfLast { it.contains('#') }

        var countEmptyGroundTiles = 0
        for (row in mostTopElf .. mostBottomElf) {
            for (col in mostLeftElf .. mostRightElf) {
                if (board[row][col] == '.') {
                    countEmptyGroundTiles++
                }
            }
        }
        return countEmptyGroundTiles
    }

    fun getProposedMoves(roundNumber: Int): Map<Pos, MutableList<Pos>> {
        val result = HashMap<Pos, MutableList<Pos>>()
        for (row in board.indices) {
            for (column in board[row].indices) {
                if (board[row][column] == '#') {
                    if (hasNeighbours(row, column)) {
                        val posTo = proposedMove(row, column, roundNumber)
                        val posFrom = Pos(row, column)
                        if (posTo != null) {
                            if (!result.contains(posTo))
                                result[posTo] = mutableListOf()
                            result[posTo]!!.add(posFrom)
                        }
                    }
                }
            }
        }
        return result.filter {it.value.size == 1}
    }

    fun moveToProposals(proposals: Map<Pos, MutableList<Pos>>) {
        for (proposal in proposals) {
            removeElf(proposal.value.first())
        }
        for (proposal in proposals) {
            putElf(proposal.key)
        }
    }

    private fun removeElf(pos: Pos) {
        board[pos.row][pos.col] = '.'
    }

    private fun putElf(pos: Pos) {
        board[pos.row][pos.col] = '#'
    }

    private fun hasNeighbours(row: Int, col: Int): Boolean {
        for (dir in Direction.values()) {
            if (board[row+dir.dRow][col+dir.dCol] == '#')
                return true
        }
        return false
    }

    private fun proposedMove(row: Int, col: Int, roundNumber: Int): Pos? {
        repeat(4) {
            if ((it + roundNumber) % 4 == 0) {
                if (board[row + Direction.N.dRow][col + Direction.N.dCol] == '.' && board[row + Direction.NE.dRow][col + Direction.NE.dCol] == '.' && board[row + Direction.NW.dRow][col + Direction.NW.dCol] == '.')
                    return Pos(row + Direction.N.dRow, col + Direction.N.dCol)
            }
            if ((it + roundNumber) % 4 == 1) {
                if (board[row + Direction.S.dRow][col + Direction.S.dCol] == '.' && board[row + Direction.SE.dRow][col + Direction.SE.dCol] == '.' && board[row + Direction.SW.dRow][col + Direction.SW.dCol] == '.')
                    return Pos(row + Direction.S.dRow, col + Direction.S.dCol)
            }
            if ((it + roundNumber) % 4 == 2) {
                if (board[row + Direction.W.dRow][col + Direction.W.dCol] == '.' && board[row + Direction.NW.dRow][col + Direction.NW.dCol] == '.' && board[row + Direction.SW.dRow][col + Direction.SW.dCol] == '.')
                    return Pos(row + Direction.W.dRow, col + Direction.W.dCol)
            }
            if ((it + roundNumber) % 4 == 3) {
                if (board[row + Direction.E.dRow][col + Direction.E.dCol] == '.' && board[row + Direction.NE.dRow][col + Direction.NE.dCol] == '.' && board[row + Direction.SE.dRow][col + Direction.SE.dCol] == '.')
                    return Pos(row + Direction.E.dRow, col + Direction.E.dCol)
            }
        }
        return null
    }

    fun print() {
        board.forEach { println(it) }
    }
}

class Pos(val row: Int, val col: Int) {
    override fun hashCode() = 1000* row + col
    override fun equals(other: Any?): Boolean {
        if (other is Pos)
            return other.row == row && other.col == col
        return super.equals(other)
    }
}

enum class Direction(val dRow: Int, val dCol: Int) {
    E(0,1),
    S(1,0),
    W(0,-1),
    N(-1,0),
    NE(-1,1),
    NW(-1,-1),
    SW(1,-1),
    SE(1,1);
}

