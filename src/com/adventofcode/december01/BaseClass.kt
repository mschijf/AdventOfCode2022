package com.adventofcode.december01

class BaseClass(inputLineList: List<String>) {

    private var calorieList = mutableListOf<List<Int>>()
    init {
        var tmp = mutableListOf<Int>()
        inputLineList.forEach {
            if (it.isBlank()) {
                calorieList.add(tmp)
                tmp = mutableListOf<Int>()
            } else {
                tmp.add(it.toInt())
            }
        }
    }

    fun maxCalories(): Int {
        return calorieList
            .map { it.sum() }
            .max()
    }

    fun topThreeCalories(): Int {
        return calorieList
            .map { it.sum() }
            .sortedDescending()
            .subList(0, 3)
            .sum()
    }
}