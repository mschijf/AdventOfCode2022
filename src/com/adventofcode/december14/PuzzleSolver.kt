package com.adventofcode.december14

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val listOfRockPath = input.inputLines
        .map{RockPath(it)}

    override fun resultPartOne(): String {
        val grid = fillGrid()

        var pos = dropSand(grid)
        var count = 0
        while(pos.y > 0) {
            grid[pos.x][pos.y] = 'o'
            pos = dropSand(grid)
            count++
        }
//        printGrid(grid, 490, 510, 0, 12)
        return count.toString()
    }

    override fun resultPartTwo(): String {

        val maxY = listOfRockPath
            .map { rockPath -> rockPath.pathList }
            .flatten()
            .maxOfOrNull { line -> max(line.startPos.y, line.endPos.y) }?:0

        val grid = fillGrid(bottomY = maxY+2)

        var count = 0
        do {
            val pos = dropSand(grid)
            grid[pos.x][pos.y] = 'o'
            count++
        } while(!(pos.x == 500 && pos.y == 0))

//        printGrid(grid, 490, 510, 0, maxY+3)
        return count.toString()
    }

    private fun dropSand(grid: Array<CharArray>): Pos {
        var sandPos = Pos(500, 0)
        while (sandPos.y < grid[sandPos.x].size-1) {
            if (grid[sandPos.x][sandPos.y + 1] == '.') {
                sandPos = Pos(sandPos.x, sandPos.y + 1)
            } else if (grid[sandPos.x - 1][sandPos.y + 1] == '.') {
                sandPos = Pos(sandPos.x - 1, sandPos.y + 1)
            } else if (grid[sandPos.x + 1][sandPos.y + 1] == '.') {
                sandPos = Pos(sandPos.x + 1, sandPos.y + 1)
            } else {
                return sandPos
            }
        }
        return Pos(-1,-1)
    }

    private fun fillGrid(bottomY: Int? = null) : Array<CharArray> {
        val grid = Array(1000) { CharArray(500) {'.'} }
        listOfRockPath.forEach {
            it.pathList.forEach {line->
                if (line.startPos.x == line.endPos.x) {
                    for (y in min(line.startPos.y, line.endPos.y) .. max(line.endPos.y, line.startPos.y) ) {
                        grid[line.startPos.x][y] = 'R'
                    }
                } else if (line.startPos.y == line.endPos.y) {
                    for (x in min(line.startPos.x, line.endPos.x) .. max(line.endPos.x, line.startPos.x) ) {
                        grid[x][line.startPos.y] = 'R'
                    }
                } else {
                    println("UNEXPECTED INPUT!!")
                }
            }
        }
        if (bottomY != null) {
            for (x in 0 until grid.size) {
                grid[x][bottomY] = 'R'
            }
        }
        return grid
    }

    private fun printGrid(grid: Array<CharArray>, minX: Int, maxX: Int, minY: Int, maxY: Int) {
        for (y in minY .. maxY) {
            for (x in minX .. maxX) {
                print(grid[x][y])
            }
            println()
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------

class RockPath(strPath: String) {
    val pathList = toLines(
        strPath
        .split(" -> ")
        .map { Pos(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }
    )

    private fun toLines(posList: List<Pos>): List<Line> {
        val result = mutableListOf<Line>()
        for (i in 1 until posList.size) {
            result.add(Line(posList[i-1], posList[i]))
        }
        return result
    }
}

class Pos(val x: Int, val y: Int)
class Line(val startPos: Pos, val endPos: Pos)