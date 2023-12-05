package day03

import println
import readInput
import java.lang.IndexOutOfBoundsException

fun main() {

    fun isAdjacentToSymbol(searchScope: List<List<Any>>, pattern: Regex, i: Int, j: Int): Pair<Boolean, Set<String>> {
        /*
        [NW][NN][NE]
        [WW][XX][EE]
        [SW][SS][SE]
         */

        val line = mapOf(
            /* nn */ runCatching { searchScope[i - 1 ] [j     ] } .getOrNull() to "${i-1 },${j}",
            /* ne */ runCatching { searchScope[i - 1 ] [j + 1 ] } .getOrNull() to "${i-1 },${j+1}",
            /* ee */ runCatching { searchScope[i     ] [j + 1 ] } .getOrNull() to "${i   },${j+1}",
            /* se */ runCatching { searchScope[i + 1 ] [j + 1 ] } .getOrNull() to "${i+1 },${j+1}",
            /* ss */ runCatching { searchScope[i + 1 ] [j     ] } .getOrNull() to "${i+1 },${j}",
            /* sw */ runCatching { searchScope[i + 1 ] [j - 1 ] } .getOrNull() to "${i+1 },${j-1}",
            /* ww */ runCatching { searchScope[i     ] [j - 1 ] } .getOrNull() to "${i   },${j-1}",
            /* nw */ runCatching { searchScope[i - 1 ] [j - 1 ] } .getOrNull() to "${i-1 },${j-1}",
        )
        val containsSymbol = line.any { pattern.matches(it.key.toString()) }
        val gearLocations = line.filter { it.key.toString() == "*" }.mapNotNull { it.value }.toSet()

        return Pair(containsSymbol, gearLocations)
    }

    fun recordNumber(num: List<Char>, numbers: MutableList<Int>) {
        // record the whole number
        val number = num.joinToString("").toInt()
        numbers.add(number)
    }


    fun part1(input: List<String>): Int {

        // convert each line to a list of char and wrap it in a bigger list
        val toBeProcessed = input.map { it.toCharArray().toList() }

        val symbolPattern = "[^a-zA-Z0-9.]".toRegex()
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
                        storeNumber = isAdjacentToSymbol(toBeProcessed, symbolPattern, i, j).first
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
        }
        return toBeSummed.sum()
    }


    fun storeGearIdx(num: List<Char>, setOfGears: MutableSet<String>,
                     pairedNums: MutableMap<String, MutableList<Int>>) {
        val number = num.joinToString("").toInt()
        setOfGears.forEach {
            if (pairedNums.containsKey(it)) {
                pairedNums[it]?.add(number)
            } else {
                pairedNums[it] = mutableListOf(number)
            }
        }
    }

    fun part2(input: List<String>): Int {

        // convert each line to a list of char and wrap it in a bigger list
        val toBeProcessed = input.map { it.toCharArray().toList() }

        val endX = toBeProcessed.size - 1
        val endY = toBeProcessed[0].size - 1

        val gearPattern = "[*]".toRegex()

        // "gearIdx" : [numA, numB] -- connected numbers by gear
        val pairedNums = mutableMapOf<String, MutableList<Int>>()

        for (i in 0..endX) {

            val num = mutableListOf<Char>()

            var storeNumber = false
            val setOfGears = mutableSetOf<String>()

            for (j in 0..endY) {

                val c = toBeProcessed[i][j]

                if (c.isDigit()) {
                    num.add(c)

                    // check surroundings for symbols if storeNumber is false
                    if (!storeNumber) {
                        val (storeNumberFlag, gearLocations) = isAdjacentToSymbol(toBeProcessed, gearPattern, i, j)
                        storeNumber = storeNumberFlag
                        setOfGears.addAll(gearLocations)
                    }

                    // check in case it's the last item in this line
                    try {
                        toBeProcessed[j + 1]
                    } catch (_: IndexOutOfBoundsException) {
                        // record the whole number
                        if (storeNumber) {
                            storeGearIdx(num, setOfGears, pairedNums)
                        }
                    }

                } else {
                    if (storeNumber) {
                        storeGearIdx(num, setOfGears, pairedNums)
                    }
                    // reset everything
                    num.clear()
                    storeNumber = false
                    setOfGears.clear()
                }
            }
        }
        val gearRatios = pairedNums.filterValues { it.size == 2 }.values.sumOf { it[0]*it[1] }

        return gearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/test_input")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day03/input")
    println(part1(input))
    check(part1(input) == 520019)

    println(part2(input))
    check(part2(input) == 75519888)

}

