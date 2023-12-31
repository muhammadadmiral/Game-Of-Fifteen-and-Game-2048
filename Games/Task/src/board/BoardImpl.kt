package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T?> = GameBoardImpl(width)

class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells = (1..width).flatMap { i ->
        (1..width).map { j -> Cell(i, j) }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        cells.find { it.i == i && it.j == j }

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Cell not found")

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { getCellOrNull(i, it) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { getCellOrNull(it, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            LEFT -> getCellOrNull(i, j - 1)
            RIGHT -> getCellOrNull(i, j + 1)
        }
    }
}

class GameBoardImpl<T>(override val width: Int) : GameBoard<T?> {
    private val cells = mutableMapOf<Cell, T?>()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                cells[Cell(i, j)] = null
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        cells.keys.find { it.i == i && it.j == j }

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Cell not found")

    override fun getAllCells(): Collection<Cell> = cells.keys

    override fun get(cell: Cell): T? = cells[cell]

    override fun set(cell: Cell, value: T?) {
        cells[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        cells.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell? =
        cells.entries.find { predicate(it.value) }?.key

    override fun any(predicate: (T?) -> Boolean): Boolean =
        cells.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean =
        cells.values.all(predicate)

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.mapNotNull { getCellOrNull(it, j) }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.mapNotNull { getCellOrNull(i, it) }

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            LEFT -> getCellOrNull(i, j - 1)
            RIGHT -> getCellOrNull(i, j + 1)
        }
}
