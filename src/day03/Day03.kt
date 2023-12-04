package day03

import println
import readInput
import java.lang.IndexOutOfBoundsException

fun main() {

    val symbolPattern = "[^a-zA-Z0-9.]".toRegex()

    fun isAdjacentToSymbol(searchScope: List<List<Any>>, pattern: Regex, i: Int, j: Int): Boolean {
        /*
        [NW][NN][NE]
        [WW][XX][EE]
        [SW][SS][SE]
         */

        val line = listOf(
            runCatching { searchScope[i - 1][j] }.getOrNull(),
            runCatching { searchScope[i - 1][j + 1] }.getOrNull(),
            runCatching { searchScope[i][j + 1] }.getOrNull(),
            runCatching { searchScope[i + 1][j + 1] }.getOrNull(),
            runCatching { searchScope[i + 1][j] }.getOrNull(),
            runCatching { searchScope[i + 1][j - 1] }.getOrNull(),
            runCatching { searchScope[i][j - 1] }.getOrNull(),
            runCatching { searchScope[i - 1][j - 1] }.getOrNull()
        )
        val containsSymbol = line.any { pattern.matches(it.toString()) }
        return containsSymbol
    }

    fun recordNumber(num: List<Char>, numbers: MutableList<Int>): MutableList<Int> {
        // record the whole number
        val number = num.joinToString("").toInt()
        numbers.add(number)
        return numbers
    }


    fun part1(input: List<String>): Int {

        // convert each line to a list of char and wrap it in a bigger list
        val toBeProcessed = input.map { it.toCharArray().toList() }

        val toBeSummed = mutableListOf<Int>()

        val endX = toBeProcessed.size - 1
        val endY = toBeProcessed[0].size - 1

        for (i in 0..endX) {

            val numbers = mutableListOf<Int>()
            val num = mutableListOf<Char>()
            var storeNumber = false

            for (j in 0..endY) {

                val c = toBeProcessed[i][j]

                if (c.isDigit()) {
                    num.add(c)

                    // check surroundings for symbols if storeNumber is false
                    if (!storeNumber) {
                        storeNumber = isAdjacentToSymbol(toBeProcessed, symbolPattern, i, j)
                    }

                    // check in case it's the last item in this line
                    try {
                        toBeProcessed[j + 1]
                    } catch (_: IndexOutOfBoundsException) {
                        // record the whole number
                        if (storeNumber) {
                            recordNumber(num, numbers)
                        }
                    }

                } else {
                    if (storeNumber) {
                        recordNumber(num, numbers)
                    }
                    // reset everything
                    num.clear()
                    storeNumber = false
                }
            }
            toBeSummed.addAll(numbers)
            // println("=====================================")
        }
        //println(toBeSummed)
        return toBeSummed.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/test_input")
//    check(part1(testInput) == 4361)
    check(part1(testInput) == 467835)

    val input = readInput("day03/input")
//    println(part1(input))
//    check(part1(input) == 520019)

}

