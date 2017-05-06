package org.kud.roleplay.command.roleplay

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import java.sql.SQLException

class CharaCreateCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.message.guild.idLong
        val userId = context.message.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tail()

            // TODO: sanity check for name

            if (!context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    context.bot.database.createRoleplayCharacter(guildId, userId, name)
                    context.replySuccess("your character \"$name\" has been created!")
                } catch (e: SQLException) {
                    LOGGER.error("Could not create character.", e)
                    context.replyFail("an error occurred while creating your character.")
                }
            } else {
                context.replyFail("you already have a character with that name.")
            }
        } else {
            context.replyFail("you haven't specified a character name.")
        }
    }

}
