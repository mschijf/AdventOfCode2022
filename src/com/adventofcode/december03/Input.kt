package com.adventofcode.december03

import java.io.File

class Input(fileName: String) {
    val inputLines = File("data/$fileName").bufferedReader().readLines()
}