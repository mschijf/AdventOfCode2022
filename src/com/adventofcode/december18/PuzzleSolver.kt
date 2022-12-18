package com.adventofcode.december18

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val cubeList = input.inputLines
        .map {it.split(",")}
        .map{Cube(it[0].toInt(), it[1].toInt(), it[2].toInt())}

    private val directions = listOf(
        Cube(-1,0,0), Cube(1,0,0),
        Cube(0,-1,0), Cube(0,1,0),
        Cube(0,0,-1), Cube(0,0,1) )

    private val minX = cubeList.minOf { cube -> cube.x }
    private val maxX = cubeList.maxOf { cube -> cube.x }
    private val minY = cubeList.minOf { cube -> cube.y }
    private val maxY = cubeList.maxOf { cube -> cube.y }
    private val minZ = cubeList.minOf { cube -> cube.z }
    private val maxZ = cubeList.maxOf { cube -> cube.z }

    private var enclosedSet = mutableSetOf <Cube>()
    private var notEnclosedSet = mutableSetOf <Cube>()

    override fun resultPartOne(): String {
        var cubeConnectedSides = 0
        for (i in 0 until cubeList.size - 1) {
            for (j in i+1 until cubeList.size) {
                if (cubeList[i].connectedTo(cubeList[j])) {
                    cubeConnectedSides+=2
                }
            }
        }
        val totalCubeSides = cubeList.size * 6 - cubeConnectedSides
        return totalCubeSides.toString()
    }

    override fun resultPartTwo(): String {
        val borders = determineOuterBorders()
        var countCubes = 0
        var cubeConnectedSides = 0
        for (cube in cubeList) {
            countCubes++
            if (countCubes % 100 == 0) {
                println("Examined $countCubes of ${cubeList.size}")
            }
            for (dir in directions) {
                if (enclosed(cube.plus(dir), mutableSetOf(), 0, borders) ) {
                    cubeConnectedSides++
                }
            }
        }
        val totalCubeSides = cubeList.size * 6 - cubeConnectedSides
        return totalCubeSides.toString()
    }

    private fun determineOuterBorders(): Set<Cube> {
        val result = mutableSetOf<Cube>()
        for (x in minX .. maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
                for (z in maxZ downTo minZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
            }
            for (y in maxY downTo minY) {
                for (z in minZ..maxZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
                for (z in maxZ downTo minZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
            }
        }

        for (x in maxX downTo  minX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
                for (z in maxZ downTo minZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
            }
            for (y in maxY downTo minY) {
                for (z in minZ..maxZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
                for (z in maxZ downTo minZ) {
                    val cube = Cube(x, y, z)
                    if (cube in cubeList) {
                        break
                    }
                    result.add(cube)
                }
            }
        }

        return result
    }

    private fun enclosed(cube: Cube, alreadyVisited: MutableSet<Cube>, level: Int, borders: Set<Cube>): Boolean {
        if (cube.x < minX || cube.x > maxX ||cube.y < minY || cube.y > maxY || cube.z < minZ || cube.z > maxZ)
            return false
        if (cube in borders)
            return false


        if (cube in enclosedSet)
            return true
        if (cube in notEnclosedSet)
            return false


        if (cube in cubeList)
            return true


        for (dir in directions) {
            val next = cube.plus(dir)

            if (!alreadyVisited.contains(next)) {
                alreadyVisited.add(next)
                if (!enclosed(next, alreadyVisited, level+1, borders)) {
                    notEnclosedSet.add(next)
                    return false
                }
            }
        }
        enclosedSet.add(cube)
        return true
    }
}

//----------------------------------------------------------------------------------------------------------------------

class Cube(val x: Int, val y: Int, val z: Int) {
    fun connectedTo(other: Cube): Boolean {
        return  (x == other.x && y == other.y && (z-other.z).absoluteValue == 1) ||
                (x == other.x && z == other.z && (y-other.y).absoluteValue == 1) ||
                (y == other.y && z == other.z && (x-other.x).absoluteValue == 1)
    }

    fun plus(dir: Cube): Cube {
        return (Cube(x+dir.x, y+dir.y, z+dir.z))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Cube)
            return super.equals(other)
        return x==other.x && y == other.y && z == other.z
    }

    override fun toString() = "($x,$y,$z)"
    override fun hashCode(): Int = 10000*x + 100*y + z

}

