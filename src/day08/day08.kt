package day08

import println
import readInput

fun main() {

    data class Node(val left: String, val right: String)

    fun createDictionary(input: List<String>, pattern: Regex) : Map<String, Node> {

        val dictionary = mutableMapOf<String, Node>()
        input.drop(2).forEach {line ->
            pattern.findAll(line)
                .map{ it.value.trim() }.toList()
                .let { (key, valueA, valueB) ->
                    dictionary[key] = Node(valueA, valueB)
                }
        }

        return dictionary

    }

    fun countStepsToTarget(commands: String, dictionary: Map<String, Node>, startKey: String, target: Regex) : Int {
        var targetNotFound = true

        var key = startKey
        var counter = 0
        while (targetNotFound) {
            commands.forEach command@{
                key = if (it.equals('L', ignoreCase = true)) {
                    dictionary[key]!!.left
                } else {
                    dictionary[key]!!.right
                }
                counter++

                if (target.matches(key)) {
                    key.println()
                    targetNotFound = false
                    return@command // break
                }
            }
        }
        return counter
    }

    fun part1(input: List<String>): Int {
        val commands = input[0]
        val neededPattern = """[A-Z]{3}""".toRegex()
        val targetPattern = """ZZZ""".toRegex()

        val dictionary = createDictionary(input, neededPattern)

        return countStepsToTarget(commands, dictionary, "AAA", targetPattern)

    }

    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/test_input")
    check(part1(testInput) == 6)
    check(part2(testInput) == 6)

    val input = readInput("day08/input")
    println(part1(input))
    check(part1(input) == 18023)

//    println(part2(input))
//    check(part2(input) == 0)
}