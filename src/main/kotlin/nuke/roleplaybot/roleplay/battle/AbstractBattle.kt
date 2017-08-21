package nuke.roleplaybot.roleplay.battle

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import nuke.roleplaybot.database.Character
import nuke.roleplaybot.util.OrderedPair

abstract class AbstractBattle(protected val channel: TextChannel,
                              pair: OrderedPair<Character>) {

    val first = pair.first
    val second = pair.second

    operator fun component1() = first
    operator fun component2() = second

    // contract methods

    abstract fun start()
    abstract fun stop()

    // JDA utility

    private val jda = channel.jda

    val Character.member get(): Member = jda.getGuildById(guildId).getMemberById(userId)

    fun Character.reply(message: String) = channel.send {
        this += ":crossed_swords: | **"
        this += member
        this += "**, "
        this += message
    }

    fun reply(message: String) = channel.send {
        this += ":crossed_swords: | **"
        this += first.member
        this += "** and **"
        this += second.member
        this += "**, "
        this += message
    }

}