package day05

import println
import readInput

fun main() {

    fun printSeedBanner(seed: Long) {
            "*******************************".println()
                "SEED IS $seed".println()
            "*******************************".println()
    }

    fun groupInputs(input: List<String>) : List<String> {

        val combo = mutableListOf<String>()
        val treasureMap = mutableListOf<String>()
        input.forEach {line ->
            if (line.isNotBlank()) {
                treasureMap.add(line)
            } else {
                combo.add(treasureMap.toString())
                treasureMap.clear()
            }
        }
        combo.add(treasureMap.toString()) // handle last entry
        return combo
    }

    fun getNumbers(treasureMap: String, pattern: Regex) : List<Long> {
        return treasureMap.let{ seedsString ->
            pattern.findAll(seedsString).map { it.value.toLong() }.toList()
        }
    }

    fun sourceToDestination(targetSeed: Long, mapDetails: List<Long>) : Long {

        val destinationStart = mapDetails[0]
        val sourceStart = mapDetails[1]
        val range = mapDetails[2]

        val sourceEnd = sourceStart+range
        val destinationEnd = destinationStart + range

        val locationOffset = targetSeed - sourceStart
        val targetDestination = destinationStart + locationOffset

        val inSource = (targetSeed in sourceStart..<sourceEnd)
        val inDestination = (targetDestination in destinationStart..destinationEnd)

        val isInMap = inSource && inDestination

        if (!isInMap) return targetSeed
        return destinationStart + locationOffset

    }

    fun part1(input: List<String>): Long {
        val regex = Regex("\\d+")
        val problemSet = groupInputs(input)
        val seedsList = getNumbers(problemSet[0], regex)

        val endLocation = mutableListOf<Long>()
        seedsList.forEach { initialSeed ->
            var seed = initialSeed
            problemSet.drop(1).forEach {treasureMap ->
                val locationMap = getNumbers(treasureMap, regex).chunked(3)
                var result = -1L
                for (mapDetails in locationMap) {
                    val mapOutput = sourceToDestination(seed, mapDetails)
                    if (mapOutput != seed) {
                        result = mapOutput
                        break
                    }
                }
                if (result == -1L) result = seed
                seed = result
            }
            endLocation.add(seed)

        }
        return endLocation.min()
    }


    fun part2(input: List<String>): Int {
        return input.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/test_input")
    check(part1(testInput) == 35L)
//    check(part2(testInput) == 0)

    val input = readInput("day05/input")
    println(part1(input))
    check(part1(input) == 621354867L)

}