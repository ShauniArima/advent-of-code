import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.measureTime

fun main() {
    fun part1BruteForce(raceInputs: List<RaceInput>) =
        raceInputs
            .map(RaceInput::winPossibilities)
            .map(List<Double>::size)
            .fold(1, Int::times)

    fun part2BruteForce(raceInput: RaceInput) =
        raceInput.winPossibilities
            .size

    fun part1(raceInputs: List<RaceInput>) =
        raceInputs
            .map(RaceInput::winPossibilitiesCount)
            .fold(1, Int::times)

    fun part2(raceInput: RaceInput) =
        raceInput.winPossibilitiesCount

    val testInputPart1 = readRaceInputs("Day06_test")
    check(part1(testInputPart1) == 288)
    val testInputPart2 = readRaceInputsAsOneRace("Day06_test")
    check(part2(testInputPart2) == 71503)

    val inputPart1 = readRaceInputs("Day06")
    val inputPart2 = readRaceInputsAsOneRace("Day06")

    val timeTakenByBruteForce = measureTime {
        part1BruteForce(inputPart1).println()
        part2BruteForce(inputPart2).println()
    }

    val timeTakenByComputation = measureTime {
        part1(inputPart1).println()
        part2(inputPart2).println()
    }

    println("Time (brute force): $timeTakenByBruteForce")
    println("Time (computation): $timeTakenByComputation")
}

fun readRaceInputs(name: String): List<RaceInput> {
    return readInput(name)
        .asSequence()
        .map {
            it.split(" ")
                .filter(String::isNotBlank)
                .drop(1)
                .map(String::toDouble)
        }
        .zipWithNext { a, b -> a.zip(b) }
        .flatten()
        .map { (time, distance) -> RaceInput(time, distance) }
        .toList()
}

fun readRaceInputsAsOneRace(name: String): RaceInput {
    return readInput(name)
        .asSequence()
        .map {
            it.split(" ")
                .filter(String::isNotBlank)
                .drop(1)
                .joinToString("")
                .toDouble()
        }
        .zipWithNext()
        .map { (time, distance) -> RaceInput(time, distance) }
        .first()
}

data class RaceInput(
    val time: Double,
    val distance: Double
) {
    val winPossibilities by lazy {
        DoubleRange(0.0, time)
            .asSequence()
            .map { holdDuration ->
                holdDuration * (time - holdDuration)
            }
            .filter { it > distance }
            .toList()
    }

    val winPossibilitiesCount by lazy {
        val distance = distance + 1

        val b1 = floor((time + sqrt(time.pow(2) - 4 * distance)) / 2)
        val b2 = ceil((time - sqrt(time.pow(2) - 4 * distance)) / 2)

        (b1 - b2 + 1).toInt()
    }
}


class DoubleRange(
    override val start: Double,
    override val endInclusive: Double
): ClosedRange<Double>, Iterable<Double> {
    override operator fun iterator(): Iterator<Double> =
        DoubleIterator(this)
}

class DoubleIterator(
    private val range: ClosedRange<Double>
) : Iterator<Double> {
    private var current = range.start

    override fun hasNext(): Boolean =
        current <= range.endInclusive

    override fun next(): Double {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}