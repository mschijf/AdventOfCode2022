package com.adventofcode.december24

import com.adventofcode.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val valley = Valley(input.inputLines)
        val minutesPassed = walkTo(valley, valley.startPos, valley.endPos)
        return minutesPassed.toString()
    }

    override fun resultPartTwo(): String {
        val valley = Valley(input.inputLines)
        val minutesPassed1 = walkTo(valley, valley.startPos, valley.endPos)
        val minutesPassed2 = walkTo(valley, valley.endPos, valley.startPos)
        val minutesPassed3 = walkTo(valley, valley.startPos, valley.endPos)
        return (minutesPassed1 + minutesPassed2 + minutesPassed3).toString()
    }

    private fun walkTo(valley: Valley, fromPos: Pos, toPos: Pos):Int {
        var minutesPassed = 0
        var candidatePerMinuteSet = setOf(fromPos)

        while (!candidatePerMinuteSet.contains(toPos)) {
            minutesPassed++
            valley.doBlizzardMove()
            candidatePerMinuteSet = candidatePerMinuteSet.map{elfPos -> valley.generateMoves(elfPos)}.flatten().toSet()
        }
        return minutesPassed
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Valley(inputLines: List<String>) {
    private val valley = inputLines.map{ str -> Array(str.length) { i -> PositionInfo(str[i]) } }
    private val maxRow = valley.size
    private val maxCol = valley[0].size

    val startPos = Pos(0, valley.first().indexOfFirst { it.isGround } )
    val endPos = Pos(valley.lastIndex, valley.last().indexOfFirst { it.isGround } )

    private fun isFreeField(row: Int, col: Int) = (row in 0 until maxRow) && (col in 0 until maxCol) && valley[row][col].blizzardList.isEmpty() && valley[row][col].isGround

    fun generateMoves(elf: Pos): List<Pos> {
        val result = if (isFreeField(elf.row, elf.col)) mutableListOf(elf) else mutableListOf()
        for (direction in Direction.values()) {
            if (isFreeField(elf.row+direction.dRow, elf.col + direction.dCol)) {
                result.add(Pos(elf.row+direction.dRow, elf.col + direction.dCol))
            }
        }
        return result
    }

    fun doBlizzardMove() {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].blizzardList.forEach {blizzard ->
                    val nextBlizzardPos = when (blizzard) {
                        '>' -> nextBlizzardPos(row, col, Direction.RIGHT)
                        '<' -> nextBlizzardPos(row, col, Direction.LEFT)
                        '^' -> nextBlizzardPos(row, col, Direction.UP)
                        'v' -> nextBlizzardPos(row, col, Direction.DOWN)
                        else -> Pos(0,0)
                    }
                    valley[nextBlizzardPos.row][nextBlizzardPos.col].addBlizzard(blizzard)
                }
            }
        }
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                valley[row][col].alignBlizzards()
            }
        }
    }

    private fun toDirection(blizzardChar: Char): Direction {
        return when (blizzardChar) {
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> throw Exception("Unexpected Blizzard Char")
        }
    }


    private fun nextBlizzardPos(row: Int, col: Int, dir: Direction): Pos {
        val newRow = row + dir.dRow
        val newCol = col + dir.dCol
        if (valley[newRow][newCol].isWall) {
            val oppRow = ((row + 2*dir.dRow + maxRow) % maxRow) + dir.dRow
            val oppCol = ((col + 2*dir.dCol + maxCol) % maxCol) + dir.dCol
            return Pos(oppRow, oppCol)
        } else {
            return Pos(newRow, newCol)
        }
    }

    fun print(candidatePerMinuteSet: Set<Pos>) {
        for (row in valley.indices) {
            for (col in valley[row].indices) {
                if (Pos(row, col) in candidatePerMinuteSet) {
                    print("E")
                } else {
                    valley[row][col].print()
                }
            }
            println()
        }
    }

    inner class PositionInfo(inputChar: Char) {
        val isWall = inputChar == '#'
        val isGround = inputChar != '#'

        val blizzardList = listOfNotNull(if (inputChar in "<>^v") inputChar else null).toMutableList()
        private val afterMoveBlizzardList = mutableListOf<Char>()

        fun addBlizzard(blizzardChar: Char) {
            afterMoveBlizzardList.add(blizzardChar)
        }

        fun alignBlizzards() {
            blizzardList.clear()
            blizzardList.addAll(afterMoveBlizzardList)
            afterMoveBlizzardList.clear()
        }

        fun print() {
            if (isWall) {
                print('#')
            } else if (blizzardList.isEmpty()) {
                print('.')
            } else if (blizzardList.size == 1) {
                print(blizzardList.first())
            } else {
                print(blizzardList.size)
            }
        }
    }

}


class Pos(val row: Int, val col: Int) {
    override fun hashCode() = 1000* row + col
    override fun equals(other: Any?) = if (other is Pos) other.row == row && other.col == col else super.equals(other)
}

enum class Direction(val dRow: Int, val dCol: Int) {
    RIGHT(0,1),
    DOWN(1,0),
    LEFT(0,-1),
    UP(-1,0);
}
