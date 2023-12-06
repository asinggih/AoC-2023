package day04

import println
import readInput
import kotlin.math.pow

fun main() {

    val cardPattern = """Card\s.*[0-9]{1,3}: """.toRegex()
    val pipeSeparator = """\|""".toRegex()
    val spaceSeparator = """\s""".toRegex()

    fun calculate(wins: Int): Int {
        return 2.0.pow(wins.toDouble()-1).toInt()
    }

    fun convertToSet(givenNumbers: String) : Set<Int> {
        return givenNumbers.trim()
            .split(spaceSeparator)
            .filter{it.isNotBlank()}
            .map { it.toInt() }
            .toSet()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val cleanedLine = line.split(cardPattern)[1].split(pipeSeparator)
            val winningNums = convertToSet(cleanedLine[0])
            val chosenNums = convertToSet(cleanedLine[1])

            val wins = chosenNums.intersect(winningNums).size
            calculate(wins)
        }
    }


    fun part2(input: List<String>): Int {
        val offset = 2

        // "$cardNum" : $howManyCopiesIncludingOriginal
        val scratchCards = mutableMapOf<Int, Int>()

        input.forEachIndexed { idx, line ->

            val cardNum = idx + 1

            var multiplier = 1
            if (scratchCards.containsKey(cardNum)) {
                scratchCards[cardNum] = scratchCards[cardNum]!! + 1
                // add multiplier to save number of copies of this particular card we have
                multiplier = scratchCards[cardNum]!!
            } else {
                scratchCards[cardNum] = 1
            }

            val cleanedLine = line.split(cardPattern)[1].split(pipeSeparator)
            val winningNums = convertToSet(cleanedLine[0])
            val chosenNums = convertToSet(cleanedLine[1])

            val wins = chosenNums.intersect(winningNums).size
            val startPoint = idx+offset
            val endPoint = wins+startPoint
            val bonus = (startPoint..<endPoint).toList()
            bonus.forEach {
                // make sure we also multiply the bonus with the copies of this particular card
                if (scratchCards.containsKey(it)) {
                    scratchCards[it] = scratchCards[it]!! + (1*multiplier)
                } else {
                    scratchCards[it] = (1*multiplier)
                }
            }
        }
        return scratchCards.values.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/test_input")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/input")
    println(part1(input))
    check(part1(input) == 17803)

    println(part2(input))
    check(part2(input) == 5554894)
}