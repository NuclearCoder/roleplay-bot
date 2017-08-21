package nuke.roleplaybot.roleplay.battle

import net.dv8tion.jda.core.entities.TextChannel
import nuke.roleplaybot.database.Character
import nuke.roleplaybot.util.OrderedPair

abstract class AbstractBattleManager {

    protected val battles = mutableMapOf<OrderedPair<Character>, AbstractBattle>()

    /**
     * Starts a battle (if none is already started) and returns the AbstractBattle object
     */
    fun startBattle(channel: TextChannel, first: Character, second: Character): AbstractBattle? {
        OrderedPair(first, second).let { pair ->
            return if (areFighting(pair)) null else {
                newBattleWith(channel, pair).also { battle ->
                    battles.put(pair, battle)
                    battle.start()
                }
            }
        }
    }

    /**
     * Stops a battle, silently.
     */
    fun stopBattle(first: Character, second: Character) {
        OrderedPair(first, second).let { pair ->
            if (areFighting(pair)) {
                battles.remove(pair)?.stop()
            }
        }
    }

    /**
     * Returns true if and only if there is a battle
     */
    fun areFighting(first: Character, second: Character) = areFighting(OrderedPair(first, second))

    fun areFighting(pair: OrderedPair<Character>) = battles.containsKey(pair)

    abstract fun newBattleWith(channel: TextChannel, pair: OrderedPair<Character>): AbstractBattle


}