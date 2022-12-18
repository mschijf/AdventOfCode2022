package com.adventofcode.mathtool

object Combination {
    /* This code is contributed by Devesh Agrawal (and altered by myself) */

    private var allCombinationsList = mutableListOf<List<Int>>()
    private fun combinationUtil(arr: IntArray, data: IntArray, start: Int, end: Int, index: Int, r: Int) {
        if (index == r) {
            allCombinationsList.add(data.toList())
            return
        }

        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            data[index] = arr[i]
            combinationUtil(arr, data, i + 1, end, index + 1, r)
            i++
        }
    }

    fun getCombinationList(size: Int, splitSize: Int): List<List<Int>> {
        val data = IntArray(splitSize)
        val arr = IntArray(size){it}
        allCombinationsList.clear()
        combinationUtil(arr, data, 0, size - 1, 0, splitSize)
        return allCombinationsList
    }
}
