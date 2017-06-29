package nuke.roleplaybot.roleplay.combat.battle

import nuke.roleplaybot.database.Character
import nuke.roleplaybot.util.OrderedPair

abstract class AbstractBattleManager {

    protected val battles = mutableMapOf<OrderedPair<Character>, AbstractBattle>()

    /**
     * Starts a battle (if none is already started) and returns the AbstractBattle object
     */
    fun startBattle(first: Character, second: Character): AbstractBattle? {
        OrderedPair(first, second).let { pair ->
            return if (areFighting(pair)) null else {
                battles.put(pair, newBattleWith(pair))
            }
        }
    }

    /**
     * Stops a battle, silently.
     */
    fun stopBattle(first: Character, second: Character) {
        battles.remove(OrderedPair(first, second))
    }

    /**
     * Returns true if and only if there is a battle
     */
    fun areFighting(first: Character, second: Character) = areFighting(OrderedPair(first, second))

    fun areFighting(pair: OrderedPair<Character>) = battles.containsKey(pair)

    abstract fun newBattleWith(pair: OrderedPair<Character>): AbstractBattle


}