package games.game2048

import board.Direction
import org.junit.Assert
import org.junit.Test

class TestMoveValues : AbstractTestGameWithSmallNumbers() {
    @Test
    fun testSimpleMove() = testAllDirections("---- ---- -2-- ----", "---- ---- ---2 ----")

    @Test
    fun testNoMove() {
        val inputString = "---- ---- ---- 2424"
        val expectedString = "---- ---- ---- 2424"
        val input = TestBoard(inputString)
        val expected = TestBoard(expectedString)
        val message = "No move expected, but 'moveValues' returned true."
        testMove(Direction.RIGHT, input, expected, false, message)
        testMove(Direction.DOWN, input.mirror(), expected.mirror(), false, message)
    }

    @Test
    fun testSeveralMoves() = testAllDirections("2--- -2-- --2- ---2", "---2 ---2 ---2 ---2")

    @Test
    fun testMovesInSomeRows() = testAllDirections("2--- ---- --2- 2424", "---2 ---- ---2 2424")

    @Test
    fun testMoveAndMerge() = testAllDirections("2-2- -2-2 --2- ---2", "---4 ---4 ---2 ---2")

    @Test
    fun testMerge() = testAllDirections("2-22 2-42 22-2 ----", "--24 -242 --24 ----")

    private fun testAllDirections(inputString: String, expectedString: String, move: Boolean = true) {
        val input = TestBoard(inputString)
        val expected = TestBoard(expectedString)
        testMove(Direction.RIGHT, input, expected, move)
        testMove(Direction.DOWN, input.mirror(), expected.mirror(), move)
    }

    private fun testMove(
        direction: Direction,
        input: TestBoard,
        expected: TestBoard,
        expectedMove: Boolean,
        message: String? = null
    ) {
        val board = createBoard(input)
        val actualMove = board.moveValues(direction)
        val result = board.toTestBoard()
        Assert.assertEquals("Incorrect move to $direction.\n" +
                "Input:\n$input\n", expected, result)
        Assert.assertEquals("$message\n" +
                "The 'moveValues' method returns incorrect result. Direction: $direction.\n" +
                "Input:\n$input\n", expectedMove, actualMove)
    }
}
