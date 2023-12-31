package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    override val initialPermutation by lazy {
        generateSolvablePermutation()
    }

    private fun generateSolvablePermutation(): List<Int> {
        var permutation = (1..15).shuffled().toMutableList()

        while (!isEven(permutation)) {
            permutation = permutation.toMutableList().apply {
                val index1 = (0 until 14).random()
                val index2 = ((index1 + 1)..14).random()
                val temp = this[index1]
                this[index1] = this[index2]
                this[index2] = temp
            }
        }

        return permutation
    }
}


