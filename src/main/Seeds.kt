package com.neelkamath.seeds

enum class Cell { ALIVE, DEAD }

typealias Grid = List<MutableList<Cell>>

class Seeds(private val rows: Int, private val columns: Int) {
    var grid = List(rows) { MutableList(columns) { Cell.values().random() } }
        private set

    init {
        if (rows < 1 || columns < 1) throw Exception("There must be at least one row and column")
    }

    constructor(grid: Grid) : this(grid.size, grid[0].size) {
        this.grid = grid
    }

    /** Whether [grid] has any [Cell.ALIVE]s. */
    fun gameIsOver() = grid.none { it.contains(Cell.ALIVE) }

    private fun clone(grid: Grid) = List(grid.size) { row -> MutableList(grid[0].size) { column -> grid[row][column] } }

    private fun northWestIsAlive(row: Int, column: Int) =
        if (row == 0 || column == 0) false else grid[row - 1][column - 1] == Cell.ALIVE

    private fun northIsAlive(row: Int, column: Int) = if (row == 0) false else grid[row - 1][column] == Cell.ALIVE

    private fun northEastIsAlive(row: Int, column: Int) =
        if (row == 0 || column == columns - 1) false else grid[row - 1][column + 1] == Cell.ALIVE

    private fun westIsAlive(row: Int, column: Int) = if (column == 0) false else grid[row][column - 1] == Cell.ALIVE

    private fun eastIsAlive(row: Int, column: Int) =
        if (column == columns - 1) false else grid[row][column + 1] == Cell.ALIVE

    private fun southWestIsAlive(row: Int, column: Int) =
        if (row == rows - 1 || column == 0) false else grid[row + 1][column - 1] == Cell.ALIVE

    private fun southIsAlive(row: Int, column: Int) =
        if (row == rows - 1) false else grid[row + 1][column] == Cell.ALIVE

    private fun southEastIsAlive(row: Int, column: Int) =
        if (row == rows - 1 || column == columns - 1) false else grid[row + 1][column + 1] == Cell.ALIVE

    private fun countNeighbors(row: Int, column: Int) = listOf(
        ::northWestIsAlive, ::northIsAlive, ::northEastIsAlive,
        ::westIsAlive, ::eastIsAlive,
        ::southWestIsAlive, ::southIsAlive, ::southEastIsAlive
    ).filter { it(row, column) }.count()

    /** Progresses [grid] to its next time step. */
    fun step() {
        val newGrid = clone(grid)
        newGrid.mapIndexed { x, row ->
            row.mapIndexed { y, column ->
                newGrid[x][y] = if (column == Cell.DEAD && countNeighbors(x, y) == 2) Cell.ALIVE else Cell.DEAD
            }
        }
        grid = newGrid
    }
}