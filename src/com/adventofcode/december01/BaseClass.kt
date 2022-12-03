package com.adventofcode.december01

import com.adventofcode.mylambdas.splitByCondition

class BaseClass(private val inputLineList: List<String>) {

    fun maxCalories(): Int {
        return inputLineList
            .splitByCondition { it.isBlank() }
            .map {it.sumOf { it.toInt() }}
            .max()
    }

    fun topThreeCalories(): Int {
        return inputLineList
            .splitByCondition { it.isBlank() }
            .map {it.sumOf { it.toInt() }}
            .sortedDescending()
            .take(3)
            .sum()
    }
}


