package nuke.roleplaybot.command.roleplay.combat

import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.Command
import nuke.roleplaybot.database.getActiveCharacter

object CombatStartCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        if (context.tokenizer.hasMore) {
            val first = context.getActiveCharacter()
            if (first != null) {
                val opponent = context.tokenizer.nextUserMention()
                if (opponent != null) {
                    val second = opponent.getActiveCharacter(context.event.guild.idLong)
                    if (second != null) {
                        // TODO: confirm
                        if (!context.bot.battles.areFighting(first, second)) {
                            context.bot.battles.startBattle(context.event.textChannel, first, second)
                        } else {
                            context.replyFail("there is already an ongoing battle between your characters.")
                            context.reply {

                            }
                        }
                    } else {
                        context.replyFail("your opponent has no active character.")
                    }
                } else {
                    context.replyFail("you haven't specified an opponent.")
                }
            } else {
                context.replyFail("you have no active character.")
            }
        } else {
            context.replyFail("you haven't specified an opponent.")
        }
    }

}
