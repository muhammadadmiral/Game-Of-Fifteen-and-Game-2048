package games.game2048

/*
 * This function moves all the non-null elements to the beginning of the list
 * (by removing nulls) and merges equal elements.
 * The parameter 'merge' specifies the way how to merge equal elements:
 * it returns a new element that should be present in the resulting list
 * instead of two merged elements.
 *
 * If the function 'merge("a")' returns "aa",
 * then the function 'moveAndMergeEqual' transforms the input in the following way:
 *   a, a, b -> aa, b
 *   a, null -> a
 *   b, null, a, a -> b, aa
 *   a, a, null, a -> aa, a
 *   a, null, a, a -> aa, a
 *
 * You can find more examples in 'TestGame2048Helper'.
*/
fun <T : Any> List<T?>.moveAndMergeEqual(merge: (T) -> T): List<T> {
        val nonNullElements = this.filterNotNull()

        val result = mutableListOf<T>()

        var i = 0
        while (i < nonNullElements.size) {
                if (i < nonNullElements.size - 1 && nonNullElements[i] == nonNullElements[i + 1]) {
                        // Merge equal elements
                        result.add(merge(nonNullElements[i]))
                        i += 2
                } else {
                        // No merge, just add the element
                        result.add(nonNullElements[i])
                        i++
                }
        }

        return result
}
