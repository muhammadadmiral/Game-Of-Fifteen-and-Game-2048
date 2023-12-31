package games.gameOfFifteen

import board.Direction
import board.createGameBoard
import games.game.Game
import board.Cell

fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val initialPermutation = initializer.initialPermutation
        var index = 0

        for (i in 1..4) {
            for (j in 1..4) {
                if (index < initialPermutation.size) {
                    board[board.getCell(i, j)] = initialPermutation[index]
                    index++
                }
            }
        }
    }

    override fun canMove() = true

    override fun hasWon(): Boolean {
        var expectedValue = 1

        // Loop melalui semua sel
        for (cell in board.getAllCells()) {
            // Periksa apakah nilai di sel sesuai dengan yang diharapkan
            if (board[cell] != expectedValue) {
                return false
            }

            // Tambahkan 1 ke nilai yang diharapkan, atau jika mencapai 15, ganti dengan null
            expectedValue = (expectedValue % 15) + 1
        }

        // Jika telah melalui semua sel tanpa kembali, permainan dianggap dimenangkan
        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = findEmptyCell()

        when (direction) {
            Direction.UP -> moveValue(Cell(emptyCell.i + 1, emptyCell.j), emptyCell)
            Direction.DOWN -> moveValue(Cell(emptyCell.i - 1, emptyCell.j), emptyCell)
            Direction.LEFT -> moveValue(Cell(emptyCell.i, emptyCell.j + 1), emptyCell)
            Direction.RIGHT -> moveValue(Cell(emptyCell.i, emptyCell.j - 1), emptyCell)
        }
    }

    private fun findEmptyCell(): Cell {
        for (i in 1..4) {
            for (j in 1..4) {
                val cell = Cell(i, j)
                if (board[cell] == null) {
                    return cell
                }
            }
        }
        throw IllegalStateException("No empty cell found")
    }

    private fun moveValue(from: Cell, to: Cell) {
        board[to] = board[from]
        board[from] = null
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}
