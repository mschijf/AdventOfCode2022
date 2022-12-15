package com.adventofcode.december15

import com.adventofcode.PuzzleSolverAbstract
import kotlin.math.absoluteValue

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {
    private val yLine = if (test) 10 else 2000000
    private val maxXY = if (test) 20 else 4000000

    override fun resultPartOne(): String {

        val sensorBeaconList = input.inputLines
            .map{Pair(Pos(it.substringAfter("Sensor at ").substringBefore(":")), Pos(it.substringAfter("closest beacon is at ")))}
        val list = sensorBeaconList
            .map{Pair(it.first, it.first.mcDistance(it.second))}
            .map {Pair(it.first.x, it.second - (it.first.y - yLine).absoluteValue)}
            .filter{ it.second >= 0 }
            .map { Pair(it.first-it.second, it.first+it.second) } //-> make a xFrom and a xTo and store these, then find the overlap between them and filter that out
            .sortedBy { it.first }

        val result = mutableListOf<Pair<Int, Int>>()
        var current = list[0]
        for (i in 1 until list.size) {
            if (list[i].first > current.second) { //2-8  en 9-10
                result.add(current)
                current = list[i]
            } else if (list[i].second <= current.second) { //2-8 en 5-7
                //doe niks, hou current en ignore list[i] en behandel de volgende
            } else if (list[i].second > current.second) { //2-8 en 5-14
                current = Pair(current.first, list[i].second)
            } else {
                println("ERRRORORR")
            }
        }
        result.add(current)

        val totalRange = result.sumOf { it.second - it.first + 1 }

        val beaconListForYline = sensorBeaconList
            .map{it.second}
            .filter{it.y == yLine}
            .map {it.x}
            .distinct()

        var countBeaconsOnYLine = 0
        beaconListForYline.forEach {xValue ->
            result.forEach { range ->
                if (xValue >= range.first && xValue <= range.second) {
                    countBeaconsOnYLine++
                }
            }
        }

        return (totalRange - countBeaconsOnYLine).toString()
    }


    override fun resultPartTwo(): String {
        val sensorBeaconList = input.inputLines
            .map{Pair(Pos(it.substringAfter("Sensor at ").substringBefore(":")), Pos(it.substringAfter("closest beacon is at ")))}

        repeat (maxXY+1) {yLineIndex ->
            val list = sensorBeaconList
                .map { Pair(it.first, it.first.mcDistance(it.second)) }
                .map { Pair(it.first.x, it.second - (it.first.y - yLineIndex).absoluteValue) }
                .filter { it.second >= 0 }
                .map {
                    Pair(
                        it.first - it.second,
                        it.first + it.second
                    )
                } //-> make a xFrom and a xTo and store these, then find the overlap between them and filter that out
                .sortedBy { it.first }

            val result = mutableListOf<Pair<Int, Int>>()
            var current = list[0]
            for (i in 1 until list.size) {
                if (list[i].first > current.second+1) { //2-8  en 10-11
                    result.add(current)
                    current = list[i]
                } else if (list[i].second <= current.second) { //2-8 en 5-7
                    //doe niks, hou current en ignore list[i] en behandel de volgende
                } else if (list[i].second > current.second) { //2-8 en 5-14
                    current = Pair(current.first, list[i].second)
                } else {
                    println("ERRRORORR")
                }
            }
            result.add(current)

            if (result.none { it.first <= 0 && it.second >= maxXY }) {
                println("Op regel $yLineIndex: $result")
                for (i in 1 until result.size) {
                    if (result[i-1].second in 0 until maxXY && result[i-1].second+2 == result[i].first) {
                        return (4000000L * (result[i-1].second+1) + yLineIndex).toString()
                    }
                }

            }

        }
        return "NO SOLUTION FOUND"
    }
}



//----------------------------------------------------------------------------------------------------------------------

class Pos(s: String) { //x=14, y=17
    val x = s.substringAfter("x=").substringBefore(",").trim().toInt()
    val y = s.substringAfter("y=").trim().toInt()
    fun mcDistance(other: Pos) = (this.x - other.x).absoluteValue + (this.y - other.y).absoluteValue
}