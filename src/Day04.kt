import kotlin.collections.ArrayDeque
import kotlin.math.pow

fun main() {
    fun part1(scratchcards: List<Scratchcard>) =
        scratchcards
            .map(Scratchcard::winningNumbersCountFromNumbers)
            .sumOf { 2.0.pow(it - 1).toInt() }

    fun part2(scratchcards: List<Scratchcard>): Int {
        val scratchcardsToPlay: ArrayDeque<Scratchcard> = scratchcards
            .toCollection(ArrayDeque())

        val scratchcardsPlayed: ArrayDeque<Scratchcard> = ArrayDeque()

        while (scratchcardsToPlay.isNotEmpty()) {
            val scratchcard = scratchcardsToPlay.removeFirst()

            val wonScratchcards = IntRange(
                scratchcard.id + 1,
                scratchcard.id + scratchcard.winningNumbersCountFromNumbers
            ).map { scratchcards[it - 1] }

            scratchcardsToPlay.addAll(wonScratchcards)
            scratchcardsPlayed.add(scratchcard)
        }

        return scratchcardsPlayed.size
    }

    val testInput = readScratchPadsFromInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readScratchPadsFromInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun readScratchPadsFromInput(name: String) =
    readInput(name)
        .map(Scratchcard::fromInput)

data class Scratchcard(
    val id: Int,
    val winningNumbers: Set<Int>,
    val numbers: Set<Int>
) {
    val winningNumbersCountFromNumbers get() = winningNumbers.intersect(numbers).count()

    fun play(scratchcards: List<Scratchcard>): List<Scratchcard> =
        when (winningNumbersCountFromNumbers) {
            0 -> emptyList()
            else -> IntRange(
                    id + 1,
                    id + winningNumbersCountFromNumbers
                )
                .map { scratchcards[it - 1] }
                .flatMap { it.play(scratchcards).plus(it) }
        }


    companion object {
        fun fromInput(input: String): Scratchcard {
            val (id, winningNumbersInput, numbersInput) =
                Regex("Card\\s+(\\d+): ([\\d\\s]*) \\| ([\\d\\s]*)")
                    .find(input)!!
                    .destructured

            return Scratchcard(
                id.toInt(),
                winningNumbersInput
                    .split(" ")
                    .filter(String::isNotBlank)
                    .map(String::toInt)
                    .toSet(),
                numbersInput
                    .split(" ")
                    .filter(String::isNotBlank)
                    .map(String::toInt)
                    .toSet(),
            )
        }
    }
}

