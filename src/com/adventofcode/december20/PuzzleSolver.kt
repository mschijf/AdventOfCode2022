package com.adventofcode.december20

import com.adventofcode.PuzzleSolverAbstract
import java.lang.Math.floorMod

fun main() {
    PuzzleSolver(test=true).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        val list = input.inputLines.mapIndexed { index, it -> Pair(it.toInt(), index) }.toMutableList()

        val cycleLength = list.size
        for (index in list.indices) {
            val orgIndex = list.indexOfFirst { it.second == index }
            val orgValue = list[orgIndex].first
            val newPair = Pair(orgValue, index)
            list.removeAt(orgIndex)
            val newIndex = orgIndex + orgValue
            if (newIndex < 0) {
                list.add(floorMod(newIndex, cycleLength-1), newPair)
            } else if (newIndex > cycleLength) {
                list.add(floorMod(newIndex, cycleLength-1), newPair)
            }  else if (newIndex == 0) {
                list.add(newPair)
            } else {
                list.add(newIndex, newPair)
            }
        }
        println("${list.map { it.first }}")

        val index0 = list.indexOfFirst { it.first == 0 }
        val value1000 = list[(index0 + 1000) % cycleLength].first
        val value2000 = list[(index0 + 2000) % cycleLength].first
        val value3000 = list[(index0 + 3000) % cycleLength].first


        return (value1000 + value2000 + value3000).toString()
    }

    override fun resultPartTwo(): String {
        val decryptionKey = 811589153L
        val list = input.inputLines.mapIndexed { index, it -> Pair(it.toLong()*decryptionKey, index) }.toMutableList()
        val cycleLength = list.size

        repeat(10) {
            for (index in list.indices) {
                val orgIndex = list.indexOfFirst { it.second == index}
                val orgValue = list[orgIndex].first
                val newPair = Pair(orgValue, index)

                list.removeAt(orgIndex)

                val newIndex = orgIndex + orgValue
                if (newIndex < 0) {
                    list.add(floorMod(newIndex, cycleLength - 1), newPair)
                } else if (newIndex > cycleLength) {
                    list.add(floorMod(newIndex, cycleLength - 1), newPair)
                } else if (newIndex == 0L) {
                    list.add(newPair)
                } else {
                    list.add(newIndex.toInt(), newPair)
                }
            }
        }

        val index0 = list.indexOfFirst { it.first == 0L }
        val value1000 = list[(index0 + 1000) % cycleLength].first
        val value2000 = list[(index0 + 2000) % cycleLength].first
        val value3000 = list[(index0 + 3000) % cycleLength].first


        return (value1000 + value2000 + value3000).toString()
    }

}

//----------------------------------------------------------------------------------------------------------------------
