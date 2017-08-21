package nuke.roleplaybot.roleplay.battle.grid

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import net.dv8tion.jda.core.entities.TextChannel
import nuke.roleplaybot.command.meta.waitResponse
import nuke.roleplaybot.database.Character
import nuke.roleplaybot.roleplay.battle.AbstractBattle
import nuke.roleplaybot.util.OrderedPair
import java.util.concurrent.ThreadLocalRandom

class GridBattle(channel: TextChannel, pair: OrderedPair<Character>) : AbstractBattle(channel, pair) {

    var turn: Int = 1
    lateinit var current: Character

    override fun start() {
        reply("a battle has started between your characters.")
        nextTurn()
    }

    override fun stop() {
        reply("the battle between your characters has been stopped.")
    }

    fun nextTurn() {
        current = getCurrentTurn()
        current.reply("it is your turn.")
        channel.send {
            this += "Grid placeholder\n"
            this += "Select your spot with coordinate notation (e.g. A6, F2...)"
        }
        waitForCoordinate { first, second ->
            channel.send { this += "Chose the ($first, $second) cell" }
        }
    }

    fun waitForCoordinate(callback: (Int, Int) -> Unit) = channel.waitResponse {
        val match = "^([a-zA-Z])([0-9])$".toRegex().matchEntire(it.strippedContent) ?: return@waitResponse
        val groups = match.groupValues

        val first = groups[1][0].toUpperCase().toInt() - 64
        val second = groups[2][0].toInt()

        it.delete().queue()
        close()

        callback(first, second)
    }

    /* /temporary/ */
    private var isFirst = ThreadLocalRandom.current().nextBoolean()

    private fun getCurrentTurn() = isFirst.let {
        isFirst = !isFirst
        if (it) first else second
    }
    /* /temporary/ */

}