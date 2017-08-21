package nuke.roleplaybot.util

import com.vdurmont.emoji.EmojiParser
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageReaction
import net.dv8tion.jda.core.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent

typealias Callback = ReactionMenu.(Member) -> Unit

class ReactionMenu(val message: Message, val choices: Map<String, Callback>,
                   val target: Member?) {

    class Builder(private val message: Message, target: Member? = null) {

        private val choices = mutableMapOf<String, Callback>()
        private val menu = ReactionMenu(message, choices, target)

        fun choice(emoteName: String, callback: Callback) {
            EmojiParser.parseToUnicode(":$emoteName:").let { emoteUnicode ->
                choices["u$emoteUnicode"] = callback
                message.addReaction(emoteUnicode).queue()
            }
        }

        fun build() {
            message.jda.addEventListener(menu)
        }

    }

    fun close() {
        message.jda.removeEventListener(this)
    }

    /* event */

    @SubscribeEvent
    fun onReaction(event: GenericMessageReactionEvent) {
        fireChoiceEvent(event.member, event.reaction)
    }

    fun fireChoiceEvent(member: Member, reaction: MessageReaction) {
        if (reaction.messageIdLong == message.idLong && !reaction.isSelf && (target == null || target == member)) {
            choices.getOrElse("u${reaction.emote.name}") { return }(member)
        }
    }

}