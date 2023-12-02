package day01

import readInput

fun main() {


    fun part1(input: List<String>): Int {
        val cleanList = input.map { it.filter { it.isDigit() } }
        // println(cleanList)
        // [12, 38, 12345, 7]
        val total = cleanList.sumOf {// concat and then sum
            "${it.first()}${it.last()}".toInt()
        }

        return total
    }

    fun isNumeric(toCheck: String): Boolean {
        return toCheck.toDoubleOrNull() != null
    }

    fun extractNumber(target: String, dic: Map<String, String>, reversed: Boolean = false) : String? {
        if (isNumeric(target)) {
            return target
        }

        // iterate char inside the given string
        for (range in 1..target.length) {

            val targetString = if (!reversed) {target} else {target.reversed()}
            val subString = targetString.substring(0, range)

            // see if substring contains a number
            dic.forEach {
                val k = if (!reversed) {it.key} else {it.key.reversed()}
                val v = if (!reversed) {it.value} else {it.value.reversed()}
                if (subString.contains(k)) {
                    return v
                }

            }
        }

        return null
    }

    fun part2(input: List<String>): Int {

        val dictionary = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )

        // convert to a clean list that are just numbers
        val regex = "(?<=[0-9])|(?=[0-9])".toRegex()
        val out = input.sumOf{line ->
            val newList = line.split(regex).filter { it.isNotBlank() }
            // println(newList)

            val firstNum = extractNumber(newList.first(), dictionary)
                ?: extractNumber(newList.drop(1).first(), dictionary)
            val secondNum = extractNumber(newList.last(), dictionary, true)
                ?: extractNumber(newList.dropLast(1).last(), dictionary, true)

            //println(firstNum)
            //println(secondNum)

            "${firstNum}${secondNum}".toInt()
        }

        return out
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/test_input")
    //check(part1(testInput) == 142)
    //check(part1(testInput) == 281)

    val input = readInput("day01/input")
    println(part1(input))
    check(part1(input) == 54561)

    println(part2(input))
    check(part2(input) == 54076)
}
