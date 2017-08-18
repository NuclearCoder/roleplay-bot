package nuke.roleplaybot.roleplay.abilities

enum class Element {
    FIRE, WATER, AIR, EARTH, LIGHT, DARK;

    enum class Effectiveness {
        NEUTRAL, WEAKER, STRONGER
    }

    private companion object {
        //  Fire → Water   |   Light
        //   ↑       ↓     |    ↕
        //  Air  ← Earth   |   Dark

        // 0: Neutral, 1: Weaker, 2: Stronger

        val effectivenessTable = arrayOf(
                intArrayOf(0, 2, 0, 0, 0, 0),
                intArrayOf(1, 0, 2, 0, 0, 0),
                intArrayOf(0, 1, 0, 2, 0, 0),
                intArrayOf(0, 0, 1, 0, 0, 0),
                intArrayOf(0, 0, 0, 0, 0, 2),
                intArrayOf(0, 0, 0, 0, 2, 0)
        )
    }

    fun against(that: Element) = effectivenessTable[this.ordinal][that.ordinal].let(Effectiveness.values()::get)
}
