package org.kud.roleplay.roleplay.experience

data class UserExperience(private var _total: Long, private var _multiplier: Double) {

    private var _levelAndExp = getLevelFor(_total)

    val multiplier get() = _multiplier

    val total get() = _total

    val level get() = _levelAndExp.first
    val levelXP get() = _levelAndExp.second

    /**
     * Increments the total XP and updates the level accordingly.
     * Returns the number of level gained.
     */
    fun increment(xp: Long): Int {
        val oldLevel = level

        _total += xp
        _levelAndExp = getLevelFor(_total)

        return level - oldLevel
    }

}