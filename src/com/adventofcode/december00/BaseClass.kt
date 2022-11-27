package com.adventofcode.december00

class BaseClass(inputLineList: List<String>) {

    init {
        var lineCount = 0
        for (line in inputLineList) {
            ++lineCount
        }
    }
}