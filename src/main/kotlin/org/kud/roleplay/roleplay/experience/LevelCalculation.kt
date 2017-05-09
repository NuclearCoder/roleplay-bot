package org.kud.roleplay.roleplay.experience

// from this level, level values grow by a constant amount
private val levelThreshold = 25

// affine exponential growth, slightly below 2^n
// this holds the value of each level on its own.
private val levelValues = LongArray(levelThreshold) { 500 + Math.pow(1.8, it.toDouble()).toLong() }
private val levelThresholdValue = levelValues.last() // this is the value of the last level before affine growth

// this holds the total experience needed to reach each level (sum of all preceeding values)
private val levelExperience = LongArray(levelThreshold + 1).apply {
    this[0] = 0
    levelValues.withIndex().forEach { (i, x) -> this[i + 1] = this[i] + x }
}
private val levelThresholdExperience = levelExperience.last()

/**
 * Returns the total experience needed to reach a level.
 */
fun getLevelExperience(level: Int) =
        levelExperience[level.coerceAtMost(levelThreshold)] +
                (level - levelThreshold).coerceAtLeast(0) * levelThresholdValue

/**
 * Returns the level and current level experience for the given total experience.
 */
fun getLevelFor(exp: Long): Pair<Int, Long> =
        when {
            exp > levelThresholdExperience -> 25 + ((exp - levelThresholdExperience) / levelThresholdValue).toInt()
            else -> (25 downTo 0).first { exp >= levelExperience[it] }
        }.let {
            level ->
            (exp - getLevelExperience(level)).let {
                levelXP ->
                Pair(level, levelXP)
            }
        }