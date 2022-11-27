package com.adventofcode.december00

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

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
        val lineCount = 0
        val inputList: MutableList<String> = ArrayList()
        try {
            val fis = FileInputStream(fileName)
            val br = BufferedReader(InputStreamReader(fis))
            var line: String
            while (br.readLine().also { line = it } != null) {
                inputList.add(line)
            }
        } catch (e: Exception) {
        }
        return inputList
    }
}