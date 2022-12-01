package com.adventofcode.december01

import java.io.File

class Input(fileName: String) {

    private val inputList = readFile("data/$fileName")

    fun getInputList(): List<String> {
        return inputList
    }

    //-----------------------------------------------------------------------------------------------------------

    fun getBaseClass(): BaseClass {
        return BaseClass(inputList)
    }

    //-----------------------------------------------------------------------------------------------------------

    private fun readFile(fileName: String): List<String> {
        return File(fileName).bufferedReader().readLines()
    }
}