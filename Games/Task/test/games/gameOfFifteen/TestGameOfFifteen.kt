package games.gameOfFifteen

import board.Direction
import org.junit.Assert
import org.junit.Test
import games.game.Game

class TestGameOfFifteen {
    private fun Game.asString(): String =
        (1..4).joinToString("\n") { i ->
            (1..4).joinToString(" ") { j ->
                get(i, j)?.let { "%2d".format(it) } ?: " -"
            }
        }

    class TestGameInitializer(
        override val initialPermutation: List<Int>
    ) : GameOfFifteenInitializer

    private fun testGame(initialPermutation: List<Int>, moves: List<Move>) {
        val game = newGameOfFifteen(TestGameInitializer(initialPermutation))
        game.initialize()

        for ((index, move) in moves.withIndex()) {
            // Use null for the initial move that doesn't have a direction
            if (move.direction == null) continue
            // Check the state after each move
            Assert.assertTrue("The move for the game of fifteen should always be possible", game.canMove())
            game.processMove(move.direction)
            val prev = moves.getOrElse(index - 1) { moves.first() }.board
            Assert.assertEquals(
                "Wrong result after pressing ${move.direction} " +
                        "for\n$prev\n",
                move.board, game.asString()
            )
        }
    }

    data class Move(
        val direction: Direction?,
        val initialBoard: String
    ) {
        val board: String = initialBoard.trimMargin()
    }

    @Test
    fun testMoves() {
        testGame(
            listOf(3, 6, 13, 15, 7, 5, 8, 4, 14, 11, 12, 1, 10, 9, 2),
            listOf(
                Move(
                    null, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11 12  1
            |10  9  2  -"""
                ),
                Move(
                    Direction.RIGHT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11 12  1
            |10  9  -  2"""
                ),
                Move(
                    Direction.DOWN, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  -  1
            |10  9 12  2"""
                ),
                Move(
                    Direction.LEFT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  -
            |10  9 12  2"""
                ),
                Move(
                    Direction.UP, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  2
            |10  9 12  -"""
                ),
                Move(
                    Direction.RIGHT, """
            | 3  6 13 15
            | 7  5  8  4
            |14 11  1  2
            |10  9  - 12"""
                )
            )
        )
    }

    @Test
    fun testWinning() {
        // Initialize the game with a shuffled initial permutation
        val initialPermutation = (1..15).toList().shuffled()
        val game = newGameOfFifteen(TestGameInitializer(initialPermutation))
        game.initialize()

        // Print the initial state
        println("Initial state:\n${game.asString()}")

        // Move tiles to win the game
        val winningMoves = mutableListOf<Move>()

        // Play the winning moves
        for (i in 1..15) {
            // Use Direction.RIGHT for all winning moves
            winningMoves.add(Move(Direction.RIGHT, game.asString()))
        }

        // Print the expected final state
        val expectedFinalState = initialPermutation.sorted().chunked(4).joinToString("\n") {
            it.joinToString(" ") { value -> "%2d".format(value) }
        }
        println("Expected final state:\n$expectedFinalState")

        // Play the winning moves
        for (move in winningMoves) {
            move.direction?.let { game.processMove(it) }
            // Print the state after each move
            println("After ${move.direction}:\n${game.asString()}")
        }

        // Assert that the player wins the game
        Assert.assertFalse(
            "The player should win when the numbers are in order",
            game.hasWon()
        )
    }
}
