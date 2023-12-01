typealias CalibrationValue = Int
typealias AmendedCalibrationDocument = List<AmendedCalibrationValue>

@JvmInline
value class AmendedCalibrationValue(private val value: String) {
    private fun getFirstDigit() = value.first().digitToInt()
    private fun getLastDigit() = value.last().digitToInt()
    fun convertStringsToInt(): AmendedCalibrationValue =
        AmendedCalibrationValue(
            value
                .replace("one", "o1e")
                .replace("two", "t2o")
                .replace("three", "t3e")
                .replace("four", "f4r")
                .replace("five", "f5e")
                .replace("six", "s6x")
                .replace("seven", "s7n")
                .replace("eight", "e8t")
                .replace("nine", "n9e")
        )

    fun removeLetters(): AmendedCalibrationValue =
        AmendedCalibrationValue(
            value.replace("[A-Za-z]".toRegex(), "")
        )

    fun toCalibrationValue(): CalibrationValue = getFirstDigit() * 10 + getLastDigit()

}

fun main() {

    fun readAmendedCalibrationDocument(name: String): AmendedCalibrationDocument = readInput(name)
        .map(::AmendedCalibrationValue)

    fun part1(amendedCalibrationDocument: AmendedCalibrationDocument) = amendedCalibrationDocument
        .map(AmendedCalibrationValue::removeLetters)
        .map(AmendedCalibrationValue::toCalibrationValue)
        .sum()

    fun part2(amendedCalibrationDocument: AmendedCalibrationDocument) = amendedCalibrationDocument
        .map(AmendedCalibrationValue::convertStringsToInt)
        .map(AmendedCalibrationValue::removeLetters)
        .map(AmendedCalibrationValue::toCalibrationValue)
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readAmendedCalibrationDocument("Day01_test_part1")
    check(part1(testInputPart1) == 142)

    val testInputPart2 = readAmendedCalibrationDocument("Day01_test_part2")
    check(part2(testInputPart2) == 281)

    val input = readAmendedCalibrationDocument("Day01")
    part1(input).println()
    part2(input).println()
}
