package com.adventofcode.december15

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val yLine = if (test) 10 else 2000000
    private val maxXY = if (test) 20 else 4000000

    private val sensorBeaconList = input.inputLines
        .map{Pair(Pos(it.substringAfter("Sensor at ").substringBefore(":")), Pos(it.substringAfter("closest beacon is at ")))}

    override fun resultPartOne(): String {

        val rangeList = getRangeList(yLine)
        val mergedRangeList = mergeRangeList(rangeList)

        val totalRange = mergedRangeList.sumOf { it.second - it.first + 1 }

        val beaconListForYline = sensorBeaconList
            .map{it.second}
            .filter{it.y == yLine}
            .map {it.x}
            .distinct()

        var countBeaconsOnYLine = 0
        beaconListForYline.forEach {xValue ->
            mergedRangeList.forEach { range ->
                if (xValue >= range.first && xValue <= range.second) {
                    countBeaconsOnYLine++
                }
            }
        }

        return (totalRange - countBeaconsOnYLine).toString()
    }


    override fun resultPartTwo(): String {

        repeat (maxXY+1) {yLineIndex ->
            val rangeList = getRangeList(yLineIndex)
            val mergeRangeList = mergeRangeList(rangeList)

            if (mergeRangeList.none { it.first <= 0 && it.second >= maxXY }) {
                println("Op regel $yLineIndex: $mergeRangeList")
                for (i in 1 until mergeRangeList.size) {
                    if (mergeRangeList[i-1].second in 0 until maxXY && mergeRangeList[i-1].second+2 == mergeRangeList[i].first) {
                        return (4000000L * (mergeRangeList[i-1].second+1) + yLineIndex).toString()
                    }
                }

            }

        }
        return "NO SOLUTION FOUND"
    }

    private fun getRangeList(yLine: Int): List<Pair<Int, Int>> {
        return sensorBeaconList
            .map{Pair(it.first, it.first.mcDistance(it.second))}
            .map {Pair(it.first.x, it.second - (it.first.y - yLine).absoluteValue)}
            .filter{ it.second >= 0 }
            .map { Pair(it.first-it.second, it.first+it.second) } //-> make a xFrom and a xTo and store these, then find the overlap between them and filter that out
            .sortedBy { it.first }
    }

    private fun mergeRangeList(rangeList: List<Pair<Int, Int>>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        var current = rangeList[0]
        for (i in 1 until rangeList.size) {
            if (rangeList[i].first > current.second) { //2-8  en 9-10
                result.add(current)
                current = rangeList[i]
            } else if (rangeList[i].second <= current.second) { //2-8 en 5-7
                //doe niks, hou current en ignore list[i] en behandel de volgende
            } else if (rangeList[i].second > current.second) { //2-8 en 5-14
                current = Pair(current.first, rangeList[i].second)
            } else {
                println("ERRRORORR")
            }
        }
        result.add(current)
        return result
    }

}



//----------------------------------------------------------------------------------------------------------------------

class Pos(s: String) { //x=14, y=17
    val x = s.substringAfter("x=").substringBefore(",").trim().toInt()
    val y = s.substringAfter("y=").trim().toInt()
    fun mcDistance(other: Pos) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}