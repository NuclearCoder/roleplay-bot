package org.kud.roleplay.command.roleplay.combat

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.getActiveCharacter

object CombatStartCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        if (context.tokenizer.hasMore) {
            val first = context.getActiveCharacter()
            if (first != null) {
                val opponent = context.tokenizer.nextUserMention()
                if (opponent != null) {
                    // TODO: confirm
                    val second = opponent.getActiveCharacter(context.event.guild.idLong)
                    if (second != null) {

                        if (!context.bot.battles.areFighting(first, second)) {
                            val battle = context.bot.battles.startBattle(first, second)

                            context.reply("battle started.")
                        } else {
                            context.replyFail("battle already started.")
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
            return
        }
        context.replyFail("you haven't specified an opponent.")
    }

}