fun main() {
    fun part1(games: Games) =
        games
            .asSequence()
            .filter { it.isPossibleWith(12, CubeColor.RED) }
            .filter { it.isPossibleWith(13, CubeColor.GREEN) }
            .filter { it.isPossibleWith(14, CubeColor.BLUE) }
            .map(Game::id)
            .sumOf(GameID::id)

    fun part2(games: Games) =
        games
            .asSequence()
            .map(Game::getFewerNumberOfCubeOfEachColor)
            .map {
                it.map { it.value }
                    .fold(1) { acc, value -> acc * value}
            }
            .sum()

    val testInputPart1 = readGamesFromInput("Day02_test_part1")
    check(part1(testInputPart1) == 8)

    val testInputPart2 = readGamesFromInput("Day02_test_part2")
    check(part2(testInputPart2) == 2286)

    val input = readGamesFromInput("Day02")
    part1(input).println()
    part2(input).println()
}

fun readGamesFromInput(name: String): Games =
    readInput(name)
        .map(Game::fromInput)

typealias Games = List<Game>
data class Game(
    val id: GameID,
    val sets: GameSets
) {
    fun isPossibleWith(count: CubeCount, color: CubeColor): Boolean =
        sets.isPossibleWith(count, color)

    fun getFewerNumberOfCubeOfEachColor(): Map<CubeColor, CubeCount> =
        sets.getFewerNumberOfCubeOfEachColor()

    companion object {
        fun fromInput(input: String): Game {
            val (idInput, cubesSetsInputs) = input.split(":")

            return Game(
                GameID.fromInput(idInput),
                GameSets.fromInput(cubesSetsInputs)
            )
        }
    }
}
@JvmInline
value class GameID(val id: Int) {
    companion object {
        fun fromInput(idInput: String): GameID =
            GameID(
                idInput
                    .removePrefix("Game")
                    .trim()
                    .toInt()
            )
    }
}
@JvmInline
value class GameSets(val sets: List<GameSet>) {
    fun isPossibleWith(count: CubeCount, color: CubeColor): Boolean =
        sets
            .all { it.isPossibleWith(count, color) }

    fun getFewerNumberOfCubeOfEachColor(): Map<CubeColor, CubeCount> =
        sets
            .flatMap { it.cubes }
            .groupBy(Cube::color, Cube::count)
            .map { it.key to it.value.max() }
            .toMap()

    companion object {
        fun fromInput(setsInput: String): GameSets =
            GameSets(
                setsInput
                    .split(";")
                    .map(GameSet::fromInput)
            )
    }
}
@JvmInline
value class GameSet(val cubes: List<Cube>) {
    fun isPossibleWith(count: CubeCount, color: CubeColor): Boolean =
        cubes.filter { it.color == color }
            .all { it.count <= count }

    companion object {
        fun fromInput(setInput: String): GameSet =
            GameSet(
                setInput
                    .split(",")
                    .map(Cube::fromInput)
            )
    }
}

data class Cube(
    val count: CubeCount,
    val color: CubeColor
) {
    companion object {
        fun fromInput(cubeInput: String): Cube {
            val (count, color) = cubeInput.trim()
                .split(" ")

            return Cube(
                count.toInt(),
                CubeColor.valueOf(color.uppercase())
            )
        }
    }
}
typealias CubeCount = Int
enum class CubeColor(val colorName: String) {
    RED("red"),
    GREEN("green"),
    BLUE("blue")
}