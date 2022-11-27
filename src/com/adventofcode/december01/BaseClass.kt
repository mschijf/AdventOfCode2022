package com.adventofcode.december01

class BaseClass(inputLineList: List<String>) {

    init {
        var lineCount = 0
        for (line in inputLineList) {
            ++lineCount
        }
    }
}