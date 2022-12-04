package com.adventofcode.general

fun getDayOfMonthFromClassName(anyObject: Any): Int {
    val monthName = "december"
    val i = anyObject.javaClass.name.indexOf(monthName)
    val j = anyObject.javaClass.name.lastIndexOf(".")
    val dayOfMonth = anyObject.javaClass.name.substring(i + monthName.length, j)
    return dayOfMonth.toInt()
}

