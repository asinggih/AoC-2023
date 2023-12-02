package day02

import readInput

fun main() {

    val gamePattern = "Game [0-9]{1,3}: ".toRegex()

    val availableMarbles = mutableMapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )

    fun getMarbleSet(balls: String) : MutableMap<String, Int> {
        val marbleList = balls.split(",")
        val gameSet = mutableMapOf(
            "red" to 0,
            "green" to 0,
            "blue" to 0,
        )
        marbleList.forEach { marble -> //  game set
            val (num, colour) = marble.trim().split(" ")
            gameSet[colour] = num.toInt()
        }
        return gameSet
    }

    fun part1(input: List<String>): Int {
        var idSum = 0
        input.forEachIndexed { idx, line -> // games
            val gameIdx = idx + 1
            val splitLine = line.split(gamePattern).filter { it.isNotEmpty() }
            val sessions = splitLine.first().split(";")

            var canPlay = true
            sessions.forEach { balls -> // game
                val gameSet = getMarbleSet(balls)
                gameSet.forEach {
                    if (it.value > availableMarbles[it.key]!!) { canPlay = false }
                }
            }

            if (canPlay) { idSum += gameIdx }
        }
        return idSum
    }


    fun part2(input: List<String>): Int {
        var totalSum = 0
        input.forEach { line -> // games
            val splitLine = line.split(gamePattern).filter { it.isNotEmpty() }
            val sessions = splitLine.first().split(";")

            val red = mutableListOf<Int>()
            val green = mutableListOf<Int>()
            val blue = mutableListOf<Int>()
            sessions.forEach { balls -> // game
                val gameSet = getMarbleSet(balls)
                gameSet["red"]?.let { red.add(it) }
                gameSet["green"]?.let { green.add(it) }
                gameSet["blue"]?.let { blue.add(it) }
            }
            val minimumSet = red.max() * green.max() * blue.max()
            totalSum += minimumSet
        }
        return totalSum
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/test_input")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day02/input")
    println(part1(input))
    check(part1(input) == 2085)

    println(part2(input))
    check(part2(input) == 79315)
}
