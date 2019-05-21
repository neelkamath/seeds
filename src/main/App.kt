package com.neelkamath.seeds

private fun prompt(message: String) = print(message).run { readLine()!! }

private fun promptSize(type: String): Int {
    val input = prompt("Enter the number of $type (\"r\" for random): ")
    if (input == "r") return 100
    val num = input.toIntOrNull() ?: promptSize(type)
    return if (num > 0) num else promptSize(type)
}

private enum class SeedType { SUPPLIED, RANDOM }

private tailrec fun promptSeedType(): SeedType = when (prompt("Enter \"s\" to supply a seed (\"r\" for random): ")) {
    "s" -> SeedType.SUPPLIED
    "r" -> SeedType.RANDOM
    else -> promptSeedType()
}

private fun isValidGrid(grid: String, columns: Int) =
    grid.isNotEmpty() && grid.length % columns == 0 && grid.all { it in listOf('A', 'D') }

private tailrec fun promptSeed(columns: Int): String {
    val grid = prompt(
        """
        Enter the seed using "A" for alive cells, and "D" for dead ones (e.g., "ADDADD" has two rows if you specified
        the seed should have three columns earlier):
        """.trimStart().replace(Regex("""\s+"""), " ")
    )
    return if (isValidGrid(grid, columns)) grid else promptSeed(columns)
}

private fun createGrid(grid: String, columns: Int) =
    grid.map { if (it == 'A') Cell.ALIVE else Cell.DEAD }.chunked(columns).map { it.toMutableList() }

fun main() {
    Player(
        when (promptSeedType()) {
            SeedType.SUPPLIED -> {
                val columns = promptSize("columns")
                Seeds(createGrid(promptSeed(columns), columns))
            }
            SeedType.RANDOM -> Seeds(promptSize("rows"), promptSize("columns"))
        }
    )
}