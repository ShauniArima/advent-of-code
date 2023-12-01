fun main() {
    fun part1(entries: List<String>): Int {
        val entriesWithoutLetters = entries
            .map { it.replace("[A-Za-z]".toRegex(), "") }

        val numberForEachEntries = entriesWithoutLetters
            .map { it.first().digitToInt() * 10 + it.last().digitToInt() }

        return numberForEachEntries.sum()
    }

    fun part2(entries: List<String>): Int {
        val entriesWithoutNumberAsStrings = entries
            .map {
                it
                    .replace("one", "o1e")
                    .replace("two", "t2o")
                    .replace("three", "t3e")
                    .replace("four", "f4r")
                    .replace("five", "f5e")
                    .replace("six", "s6x")
                    .replace("seven", "s7n")
                    .replace("eight", "e8t")
                    .replace("nine", "n9n")
            }

        return part1(entriesWithoutNumberAsStrings)
    }

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readInput("Day01_test_part1")
    check(part1(testInputPart1) == 142)
    val testInputPart2 = readInput("Day01_test")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
