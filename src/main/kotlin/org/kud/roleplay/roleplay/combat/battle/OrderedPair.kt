package org.kud.roleplay.roleplay.combat.battle

import org.kud.roleplay.database.Character

class OrderedPair(first: Character, second: Character) {

    val first: Character
    val second: Character

    init {
        val lesser = first < second
        this.first = if (lesser) first else second
        this.second = if (lesser) second else first
    }

    override fun equals(other: Any?) = when (other) {
        is OrderedPair -> first == other.first && second == other.second
        else -> super.equals(other)
    }

    override fun hashCode() = 31 * (31 + first.hashCode()) + second.hashCode()

}
