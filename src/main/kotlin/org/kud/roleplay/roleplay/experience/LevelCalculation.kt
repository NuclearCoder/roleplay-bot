package org.kud.roleplay.roleplay.experience

private val levelThreshold = 25

private val levelValues = LongArray(levelThreshold) { 500 + Math.pow(1.8, it.toDouble()).toLong() }
private val levelThresholdValue = levelValues.last()

private val levelExperience = LongArray(levelThreshold).apply {
    this[0] = levelValues[0]
    (1..levelThreshold - 1).forEach {
        this[it] = this[it - 1] + levelValues[it] // accumulated sums
    }
}
private val levelThresholdExperience = levelExperience.last()

fun getLevelExperience(level: Int) =
        levelExperience[level.coerceAtMost(levelThreshold) - 1] +
                (level - levelThreshold).coerceAtLeast(0) * levelThresholdValue

fun getLevelFor(exp: Long): Pair<Int, Long> {
    val level = if (exp > levelThresholdExperience) {
        25 + ((exp - levelThresholdExperience) / levelThresholdValue).toInt()
    } else {
        (25 downTo 1).first { exp > levelExperience[it] }
    }
    val levelXP = exp - getLevelExperience(level)
    return Pair(level, levelXP)
}