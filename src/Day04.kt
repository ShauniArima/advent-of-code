import kotlin.math.pow

fun main() {
    fun part1(scratchcards: List<Scratchcard>) =
        scratchcards
            .map(Scratchcard::winningNumbersCountFromNumbers)
            .sumOf { 2.0.pow(it - 1).toInt() }

    fun part2(scratchcards: List<Scratchcard>): Int {
        val cachedPlayResults = HashMap<Int, List<Scratchcard>>()

        return scratchcards
            .parallelStream()
            .flatMap { it.play(scratchcards, cachedPlayResults).stream() }
            .toList()
            .size.plus(scratchcards.size)
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

    fun play(
        scratchcards: List<Scratchcard>,
        cachedPlayResults: HashMap<Int, List<Scratchcard>>
    ): List<Scratchcard> =
        when (cachedPlayResults.contains(id)) {
            true -> cachedPlayResults.getValue(id)
            else -> {
                val result = when (winningNumbersCountFromNumbers) {
                    0 -> emptyList()
                    else -> IntRange(
                        id + 1,
                        id + winningNumbersCountFromNumbers
                    )
                        .map { scratchcards[it - 1] }
                        .flatMap { it.play(scratchcards, cachedPlayResults).plus(it) }
                }

                cachedPlayResults[id] = result

                result
            }

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

