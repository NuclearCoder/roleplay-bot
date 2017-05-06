package org.kud.roleplay.command.roleplay

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import java.sql.SQLException

class CharaUpdateCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.message.guild.idLong
        val userId = context.message.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tailUntil { it == '\n' }

            if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                if (context.tokenizer.hasMore) {
                    val content = context.tokenizer.tail()

                    // TODO: maybe sanity check on content

                    try {
                        context.bot.database.updateRoleplayCharacter(guildId, userId, name, content)
                        context.replySuccess("your character \"$name\" has been updated!")
                    } catch (e: SQLException) {
                        LOGGER.error("Could not update character.", e)
                        context.replyFail("an error occurred while updating your character.")
                    }
                } else {
                    context.replyFail("you haven't specified a character description/bio.\nEnter your bio after the first line break. (Shift+ENTER to begin a new line)")
                }
            } else {
                context.replyFail("you have no character with that name.")
            }
        } else {
            context.replyFail("you haven't specified a character name.")
        }
    }

}
