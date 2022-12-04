package com.adventofcode.december04

import com.adventofcode.general.Input

class PuzzleSolution(private val input: Input) {

    fun resultPartOne(): Int {
        return input.inputLines
            .map{it.split(",").map{Section(it)}}
            .map{SectionPair(it[0], it[1])}
            .filter{it.hasFullyOverlap()}
            .count()
    }

    fun resultPartTwo(): Int {
        return input.inputLines
            .map{it.split(",").map{Section(it)}}
            .map{SectionPair(it[0], it[1])}
            .filter{it.hasOverlap()}
            .count()
    }
}

class SectionPair(
    val first: Section,
    val second: Section) {

    fun hasFullyOverlap() = first.fullyOverlappedBy(second) || second.fullyOverlappedBy(first)
    fun hasOverlap() = first.hasOverlapWith(second)
}

class Section(input:String) { //"2-3"
    private val splitInput = input.split("-")
    val begin: Int = splitInput[0].toInt()
    val end: Int = splitInput[1].toInt()

    fun fullyOverlappedBy(other: Section) = this.begin >= other.begin && this.end <= other.end
    fun hasOverlapWith(other: Section) = this.begin <= other.end && this.end >= other.begin
}
