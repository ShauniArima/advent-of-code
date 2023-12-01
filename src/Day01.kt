fun main() {

    fun part1(amendedCalibrationDocument: AmendedCalibrationDocument) =
        amendedCalibrationDocument
            .map(AmendedCalibrationValue::removeLetters)
            .map(AmendedCalibrationValue::toCalibrationValue)
            .sum()

    fun part2(amendedCalibrationDocument: AmendedCalibrationDocument) =
        amendedCalibrationDocument
            .map(AmendedCalibrationValue::convertIntegerNamesToStringWithAnInteger)
            .map(AmendedCalibrationValue::removeLetters)
            .map(AmendedCalibrationValue::toCalibrationValue)
            .sum()

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readAmendedCalibrationDocumentFromInput("Day01_test_part1")
    check(part1(testInputPart1) == 142)

    val testInputPart2 = readAmendedCalibrationDocumentFromInput("Day01_test_part2")
    check(part2(testInputPart2) == 281)

    val input = readAmendedCalibrationDocumentFromInput("Day01")
    part1(input).println()
    part2(input).println()
}

typealias CalibrationValue = Int
typealias AmendedCalibrationDocument = List<AmendedCalibrationValue>

fun readAmendedCalibrationDocumentFromInput(name: String): AmendedCalibrationDocument =
    readInput(name)
        .map(::AmendedCalibrationValue)

@JvmInline
value class AmendedCalibrationValue(private val value: String) {
    private val firstDigit get() = value.first().digitToInt()
    private val lastDigit get() = value.last().digitToInt()

    companion object {
        private val LETTERS_REGEX = "[A-Za-z]".toRegex()
        private val INTEGER_NAMES_TO_STRING_WITH_AN_INTEGER = mapOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "f4r",
            "five" to "f5e",
            "six" to "s6x",
            "seven" to "s7n",
            "eight" to "e8t",
            "nine" to "n9e"
        )

        fun convertIntegerNamesToStringWithAnInteger(amendedCalibrationValue: AmendedCalibrationValue): AmendedCalibrationValue =
            AmendedCalibrationValue(
                INTEGER_NAMES_TO_STRING_WITH_AN_INTEGER
                    .entries
                    .fold(amendedCalibrationValue.value) { value, (integerName, stringWithAnInteger) ->
                        value.replace(integerName, stringWithAnInteger)
                    }
            )

        fun removeLetters(amendedCalibrationValue: AmendedCalibrationValue): AmendedCalibrationValue =
            AmendedCalibrationValue(
                amendedCalibrationValue.value.replace(LETTERS_REGEX, "")
            )

        fun toCalibrationValue(amendedCalibrationValue: AmendedCalibrationValue): CalibrationValue =
            amendedCalibrationValue.firstDigit * 10 + amendedCalibrationValue.lastDigit
    }
}
