package com.adventofcode.december22

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Exception
import kotlin.math.sqrt

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val pathSteps = input.inputLines.last().split(Regex("[RL]")).map { it.toInt() }
    private val pathDirs = input.inputLines.last().filter { "RL".contains(it)}.map{ it }

    override fun resultPartOne(): String {
        val board = Board(input.inputLines.dropLast(2))
        var direction = Direction.RIGHT

        var dirIndex = 0
        for (stepSize in pathSteps) {
            board.doSteps(direction, stepSize)
            if (dirIndex < pathDirs.size) {
                direction = direction.turn(pathDirs[dirIndex])
                dirIndex++
            }
        }
        return ((board.getWalker().row+1)*1000 + (board.getWalker().col+1)*4 + direction.facingNumber).toString()
    }

    override fun resultPartTwo(): String {
        val board = Board(input.inputLines.dropLast(2))
        var direction = Direction.RIGHT

        var dirIndex = 0
        for (stepSize in pathSteps) {
            direction  = board.doStepsPart2(direction, stepSize, test)
//            println()
//            println("After step $dirIndex ===============================================================")
//            board.print()
            if (dirIndex < pathDirs.size) {
                direction = direction.turn(pathDirs[dirIndex])
                dirIndex++
            }
        }
//        board.print()
        return ((board.getWalker().row+1)*1000 + (board.getWalker().col+1)*4 + direction.facingNumber).toString()
    }


}

class Board(inputLines: List<String>) {
    private val width = inputLines.maxOf { it.length }
    private val height = inputLines.size
    private val board = inputLines
        .map { str -> CharArray(width) { i -> if (i < str.length) str[i] else ' ' } }

    private val cubeFaceWidth = sqrt(board.sumOf { row -> row.count { it != ' ' } } / 6.0 + 0.5).toInt()
    private val cubeFaces = makeCubeFaces()

    private var walker = Pos(0,0).nextPos(Direction.RIGHT)
    fun getWalker() = walker

    private val walkerPath: MutableList<Pos> = mutableListOf(Pos(walker.row, walker.col))

    fun doSteps(dir: Direction, stepLength: Int) {
        repeat(stepLength) {
            val nextPos = walker.nextPos(dir)
            if (isWall(nextPos)) {
                return
            }
            walker = nextPos
        }
    }

    fun doStepsPart2(dir: Direction, stepLength: Int, test:Boolean): Direction {
        var lastDir = dir
        repeat(stepLength) {
            val nextPair = walker.nextPosPart2(lastDir, test)
            val newPos = nextPair.first
            val newDir = nextPair.second
            if (newPos.col < 0 || newPos.row < 0 || newPos.col >= width || newPos.row >= height) {
                throw Exception("Martin throws error: Index out of bounds")
            }
            if (isWall(newPos)) {
                return lastDir
            }
            lastDir = newDir
            walker = newPos
            walkerPath.add(Pos(walker.row, walker.col))
        }
        return lastDir
    }

    private fun isWall(pos: Pos) = board[pos.row][pos.col] == '#'

    fun print() {
        board.forEachIndexed() {rowIndex, it ->
            it.forEachIndexed { colIndex, cell ->
                if (Pos(rowIndex, colIndex) in walkerPath)
                    print("${'A' + walkerPath.indexOfFirst { it == Pos(rowIndex, colIndex) }}")
                else
                    print(cell)
            }
            println()
        }

//        cubeFaces.forEach {
//            it.forEach { number -> print(number) }
//            println()
//        }
    }


    private fun makeCubeFaces(): List<IntArray> {
        val result = List(board.size){IntArray(board[0].size)}

        var cubeBaseNumber = 1
        for (cubeRow in 0 until height / cubeFaceWidth ) {
            val totalPhacesInLine = board[cubeRow*cubeFaceWidth].count{it != ' '} / cubeFaceWidth
            for (row in cubeRow*cubeFaceWidth until (cubeRow+1)*cubeFaceWidth) {
                var count = 0
                for (col in board[row].indices) {
                    if (board[row][col] != ' ') {
                        val cubeLineNumber =  (count / cubeFaceWidth)
                        result[row][col] = cubeBaseNumber + cubeLineNumber
                        count++
                    }
                }
            }
            cubeBaseNumber += totalPhacesInLine
        }
        return result
    }

    inner class Pos(val row: Int, val col: Int) {

        override fun hashCode(): Int {
            return 1000*row + col
        }

        override fun equals(other: Any?): Boolean {
            if (other is Pos)
                return row == other.row && col == other.col
            return super.equals(other)
        }

        override fun toString(): String {
            return "($row, $col)"
        }

        fun nextPos(dir: Direction): Pos {
            var newRow = row
            var newCol = col
            do {
                newRow = (newRow + dir.dRow + height) % height
                newCol = (newCol + dir.dCol + width) % width
            } while (board[newRow][newCol] == ' ')
            return Pos(newRow, newCol)
        }

        fun cubeFaceChangeExample(cubeFaceNumber: Int, dir: Direction) : Pair<Int, Int> {
            return when (cubeFaceNumber) {
                1 -> when (dir) {
                    Direction.UP -> Pair(2, 180)
                    Direction.DOWN -> Pair(4, 0)
                    Direction.LEFT -> Pair(3, 90)
                    Direction.RIGHT -> Pair(6, 180)
                }
                2 -> when (dir) {
                    Direction.UP -> Pair(1, 180)
                    Direction.DOWN -> Pair(5, 180)
                    Direction.LEFT -> Pair(6, 90)
                    Direction.RIGHT -> Pair(3, 0)
                }
                3 -> when (dir) {
                    Direction.UP -> Pair(1, 270)
                    Direction.DOWN -> Pair(5, 90)
                    Direction.LEFT -> Pair(2, 0)
                    Direction.RIGHT -> Pair(4, 0)
                }
                4 -> when (dir) {
                    Direction.UP -> Pair(1, 0)
                    Direction.DOWN -> Pair(5, 0)
                    Direction.LEFT -> Pair(3, 0)
                    Direction.RIGHT -> Pair(6, 270)
                }
                5 -> when (dir) {
                    Direction.UP -> Pair(4, 0)
                    Direction.DOWN -> Pair(2, 180)
                    Direction.LEFT -> Pair(3, 270)
                    Direction.RIGHT -> Pair(6, 0)
                }
                6 -> when (dir) {
                    Direction.UP -> Pair(4, 90)
                    Direction.DOWN -> Pair(2, 270)
                    Direction.LEFT -> Pair(5, 0)
                    Direction.RIGHT -> Pair(1, 180)
                }

                else -> throw Exception("RAARRR")
            }
        }

        fun cubeFaceChangeReal(cubeFaceNumber: Int, dir: Direction) : Pair<Int, Int> {
            return when (cubeFaceNumber) {
                1 -> when (dir) {
                    Direction.UP -> Pair(6, 270)
                    Direction.DOWN -> Pair(3, 0)
                    Direction.LEFT -> Pair(4, 180)
                    Direction.RIGHT -> Pair(2, 0)
                }
                2 -> when (dir) {
                    Direction.UP -> Pair(6, 360)
                    Direction.DOWN -> Pair(3, 270)
                    Direction.LEFT -> Pair(1, 0)
                    Direction.RIGHT -> Pair(5, 180)
                }
                3 -> when (dir) {
                    Direction.UP -> Pair(1, 0)
                    Direction.DOWN -> Pair(5, 0)
                    Direction.LEFT -> Pair(4, 90)
                    Direction.RIGHT -> Pair(2, 90)
                }
                4 -> when (dir) {
                    Direction.UP -> Pair(3, 270)
                    Direction.DOWN -> Pair(6, 0)
                    Direction.LEFT -> Pair(1, 180)
                    Direction.RIGHT -> Pair(5, 0)
                }
                5 -> when (dir) {
                    Direction.UP -> Pair(3, 0)
                    Direction.DOWN -> Pair(6, 270)
                    Direction.LEFT -> Pair(4, 0)
                    Direction.RIGHT -> Pair(2, 180)
                }
                6 -> when (dir) {
                    Direction.UP -> Pair(4, 0)
                    Direction.DOWN -> Pair(2, 360)
                    Direction.LEFT -> Pair(1, 90)
                    Direction.RIGHT -> Pair(5, 90)
                }

                else -> throw Exception("RAARRR")
            }
        }


        fun legal(newRow:Int, newCol: Int) = newRow in 0 until height && newCol in 0 until width

        fun nextPosPart2(dir: Direction, test: Boolean): Pair<Pos, Direction> {
            val newRow = row + dir.dRow
            val newCol = col + dir.dCol

            val currentFace = cubeFaces[row][col]
            val newCubeFacePair = if (test) cubeFaceChangeExample(currentFace, dir) else cubeFaceChangeReal(currentFace, dir)
            val newFace = newCubeFacePair.first

            val faceRow = row - startRowOfCubeFace(currentFace)
            val faceCol = col - startColOfCubeFace(currentFace)
            if (newRow < 0 || (legal(newRow, newCol) && board[newRow][newCol] == ' ' && dir == Direction.UP) ) {
                //up
                return when (newCubeFacePair.second) {
//                     90 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - faceCol - 1, col=startColOfCubeFace(newFace) + cubeFaceWidth - 1), dir.turn('L'))
//                    180 -> Pair(Pos(row=startRowOfCubeFace(newFace)                              , col=startColOfCubeFace(newFace) + cubeFaceWidth - faceCol - 1), dir.turn('L').turn('L'))
                    270 -> Pair(Pos(row=startRowOfCubeFace(newFace) + faceCol                    , col=startColOfCubeFace(newFace)), dir.turn('R'))
                    360 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - 1          , col=startColOfCubeFace(newFace) + faceCol), dir.turn('O'))
                    else -> throw Exception("Die ${newCubeFacePair.second} had ik niet verwacht (up)")
                }
            } else if (newRow >= height || (legal(newRow, newCol) && board[newRow][newCol] == ' ' && dir == Direction.DOWN)) {
                //down
                return when (newCubeFacePair.second) {
//                     90 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - faceCol - 1, col=startColOfCubeFace(newFace)), dir.turn('L'))
//                    180 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - 1          , col=startColOfCubeFace(newFace) + cubeFaceWidth - faceCol - 1), dir.turn('L').turn('L'))
                    270 -> Pair(Pos(row=startRowOfCubeFace(newFace) + faceCol                    , col=startColOfCubeFace(newFace) + cubeFaceWidth - 1), dir.turn('R'))
                    360 -> Pair(Pos(row=startRowOfCubeFace(newFace)                              , col=startColOfCubeFace(newFace) + faceCol), dir.turn('O'))
                    else -> throw Exception("Die ${newCubeFacePair.second} had ik niet verwacht (down)")
                }
            } else if (newCol < 0 || (legal(newRow, newCol) && board[newRow][newCol] == ' ' && dir == Direction.LEFT)) {
                //left
                return when (newCubeFacePair.second) {
                     90 -> Pair(Pos(row=startRowOfCubeFace(newFace)                              , col=startColOfCubeFace(newFace) + faceRow), dir.turn('L'))
                    180 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - faceRow - 1, col=startColOfCubeFace(newFace) ), dir.turn('L').turn('L'))
//                    270 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - 1          , col=startColOfCubeFace(newFace) + faceRow), dir.turn('R'))
                    else -> throw Exception("Die ${newCubeFacePair.second} had ik niet verwacht (left)")
                }
            } else if (newCol >= width || (legal(newRow, newCol) && board[newRow][newCol] == ' ' && dir == Direction.RIGHT)) {
                //right
                return when (newCubeFacePair.second) {
                     90 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - 1          , col=startColOfCubeFace(newFace) + faceRow), dir.turn('L'))
                    180 -> Pair(Pos(row=startRowOfCubeFace(newFace) + cubeFaceWidth - faceRow - 1, col=startColOfCubeFace(newFace) + cubeFaceWidth - 1), dir.turn('L').turn('L'))
//                    270 -> Pair(Pos(row=startRowOfCubeFace(newFace)                              , col=startColOfCubeFace(newFace) + cubeFaceWidth - faceRow - 1), dir.turn('R'))
                    else -> throw Exception("Die ${newCubeFacePair.second} had ik niet verwacht (Right)")
                }
            } else if (board[newRow][newCol] == ' ') {
                throw Exception("ONVERWACHT IN nextSTeps2")
            } else {
                return Pair(Pos(newRow, newCol), dir)
            }
        }

        private fun startRowOfCubeFace(cubeFaceNumber: Int): Int {
            return cubeFaces.indexOfFirst { it.contains(cubeFaceNumber) }
        }

        private fun startColOfCubeFace(cubeFaceNumber: Int): Int {
            return cubeFaces.first { it.contains(cubeFaceNumber) }.indexOfFirst { it == cubeFaceNumber }
        }
    }
}



enum class Direction(val dRow: Int, val dCol: Int, val facingNumber: Int) {
    RIGHT(0,1, 0),
    DOWN(1,0, 1),
    LEFT(0,-1, 2),
    UP(-1,0, 3);

    fun turn(turnChar: Char): Direction {
        when (turnChar) {
            'R' -> return Direction.values()[(this.ordinal + 1) % Direction.values().size]
            'L' -> return Direction.values()[(this.ordinal + Direction.values().size - 1) % Direction.values().size]
            'O' -> return this //360 graden
            'U' -> return Direction.values()[(this.ordinal + 2) % Direction.values().size]
            else -> throw Exception("ERRORRRR")
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

