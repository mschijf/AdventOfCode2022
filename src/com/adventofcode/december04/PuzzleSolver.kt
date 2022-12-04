package com.adventofcode.december04

import com.adventofcode.general.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=false).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): String {
        return input.inputLines
            .map { it -> it.split(",").map { Section(it) } }
            .map { SectionPair(it[0], it[1]) }
            .count { it.hasFullyOverlap() }
            .toString()
    }

    override fun resultPartTwo(): String {
        return input.inputLines
            .map { it -> it.split(",").map { Section(it) } }
            .map { SectionPair(it[0], it[1]) }
            .count { it.hasOverlap() }
            .toString()
    }
}

class SectionPair(
    private val first: Section,
    private val second: Section) {

    fun hasFullyOverlap() = first.fullyOverlappedBy(second) || second.fullyOverlappedBy(first)
    fun hasOverlap() = first.hasOverlapWith(second)
}

class Section(input:String) { //"2-3"
    private val splitInput = input.split("-")
    private val begin: Int = splitInput[0].toInt()
    private val end: Int = splitInput[1].toInt()

    fun fullyOverlappedBy(other: Section) = this.begin >= other.begin && this.end <= other.end
    fun hasOverlapWith(other: Section) = this.begin <= other.end && this.end >= other.begin
}
