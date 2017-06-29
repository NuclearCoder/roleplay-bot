package nuke.roleplaybot.command.meta

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import nuke.roleplaybot.bot.RoleplayBot
import nuke.roleplaybot.util.MessageTokenizer

class CommandContext(val event: MessageReceivedEvent,
                     val bot: RoleplayBot,
                     val botOwner: User,
                     val message: Message,
                     val command: String,
                     val tokenizer: MessageTokenizer) {

    inline fun reply(emote: String = REPLY_SUCCESS, crossinline then: MessageBuilder.() -> Unit) {
        event.channel.send {
            replyPrefix(event.member, emote)
            then()
        }
    }

    fun reply(emote: String, content: String) = reply(emote) { this += content }

    fun reply(content: String) = reply(REPLY_SUCCESS, content)
    fun replyFail(content: String) = reply(REPLY_FAILURE, content)

    fun replyMissingArguments(details: String) = reply(REPLY_FAILURE) {
        this += "missing arguments: "
        this += details
    }

}