package org.kud.roleplay.roleplay.combat

import org.kud.roleplay.database.Character
import org.kud.roleplay.util.OrderedPair

abstract class AbstractBattleManager {

    protected val battles = mutableMapOf<OrderedPair<Character>, AbstractBattle>()

    /**
     * Starts a battle (if none is already started) and returns the AbstractBattle object
     */
    fun startBattle(first: Character, second: Character): AbstractBattle? {
        val pair = OrderedPair(first, second)
        return if (areFighting(pair)) null else {
            battles.put(pair, newBattleWith(pair))
        }
    }

    /**
     * Returns true if and only if there is a battle
     */
    fun areFighting(first: Character, second: Character) = areFighting(OrderedPair(first, second))

    fun areFighting(pair: OrderedPair<Character>) = battles.containsKey(pair)


    abstract fun newBattleWith(pair: OrderedPair<Character>): AbstractBattle


}