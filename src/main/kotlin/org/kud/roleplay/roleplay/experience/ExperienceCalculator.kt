package org.kud.roleplay.roleplay.experience

class ExperienceCalculator {

    //TODO highly work-in-progress, needs a lot of tweaking and mostly trial-and-error

    /*
    Experience gained by message has a cap set by the maxGain constant.
    XP is calculated with several components, each weighted by their respective constants:

    - length: weighs 70% in the final gain
        Length XP follows inverse exponential growth, see the corresponding JavaDoc for more information.
        The constant lengthHalfLength is the word count for half the maximum gain.
        The current value for that constant is decided from the average number of words in a "wall" of text.
        Inverse exponential growth means the gain will increase more slowly as it reaches that average length.

    - typography: weighs 30% in the final gain
        TBD

     */

    companion object {

        const val maxGain = 100

        const val wLength = 0.7
        const val wTypography = 0.3

        const val lengthMaxGain = wLength * maxGain
        const val lengthHalfLength = 100
        const val lengthCharacLength = lengthHalfLength / 0.693147180559945309  // the literal constant here is ln(2)

        const val typoMaxGain = wTypography * maxGain

    }

    /**
     * f(n) = M * (1 - exp(-n / T))
     *
     * Inverse exponential will grow faster for lower values and slower for higher values,
     * this means long vs longer messages won't gain as much more experience as short vs long messages.
     */
    private fun getLengthExp(n: Int) =
            (lengthMaxGain * (1 - Math.exp(-n.toDouble() / lengthCharacLength))).toInt()

    /**
     * To be done, for now give full XP everytime.
     */
    private fun getTypographyExp() =
            typoMaxGain

    fun calculate(content: String): Long {
        var wordCount: Int = 0

        content.forEachWord {
            wordCount++
        }

        val expLength = getLengthExp(wordCount)
        val expTypography = getTypographyExp()

        val finalExp = (wLength * expLength + wTypography * expTypography).toLong()

        // perhaps add a random amount there before coercing?

        return finalExp.coerceIn(0L..maxGain)
    }

    // since each part requires word-by-word reading, this method will allow to traverse the string only once.
    fun String.forEachWord(consumer: (String) -> Unit) {
        split("\\s+".toRegex()).forEach(consumer)
    }

}