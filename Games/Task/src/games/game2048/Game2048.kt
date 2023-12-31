package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val newValue = initializer.nextValue(this)
    newValue?.let { (cell, value) -> this[cell] = value }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val valuesBeforeMove = rowOrColumn.map { this[it] }
    val mergedValues = valuesBeforeMove.moveAndMergeEqual { it * 2 }

    for (i in rowOrColumn.indices) {
        this[rowOrColumn[i]] = mergedValues.getOrNull(i)
    }

    return valuesBeforeMove != mergedValues
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game.
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    var moved = false

    for (index in 1..width) {
        val rowOrColumn = when (direction) {
            Direction.UP -> getColumn(1..width, index)
            Direction.DOWN -> getColumn(width downTo 1, index)
            Direction.LEFT -> getRow(index, 1..width)
            Direction.RIGHT -> getRow(index, width downTo 1)
        }

        val valuesBeforeMove = rowOrColumn.map { this[it] }
        val mergedValues = valuesBeforeMove.moveAndMergeEqual { it * 2 }

        // Check if there are any non-null values
        val hasNonNullValues = valuesBeforeMove.any { it != null }

        if (hasNonNullValues) {
            for (i in rowOrColumn.indices) {
                this[rowOrColumn[i]] = mergedValues.getOrNull(i)
            }

            // Check if the values were moved
            moved = moved || valuesBeforeMove != mergedValues
        }
    }

    return moved
}


