package nuke.roleplaybot.command.meta

import club.minnced.kjda.plusAssign
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Member

val REPLY_SUCCESS = ":white_check_mark:"
val REPLY_FAILURE = ":negative_squared_cross_mark:"

/**
 * Appends the prefix for a reply.
 * "emote | **name**, "
 */
fun MessageBuilder.replyPrefix(member: Member, emote: String) {
    this += emote
    this += " | **"
    this += member.effectiveName
    this += "**, "
}