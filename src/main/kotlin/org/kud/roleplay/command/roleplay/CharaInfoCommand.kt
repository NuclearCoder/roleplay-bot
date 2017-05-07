package org.kud.roleplay.command.roleplay

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import java.sql.SQLException

class CharaInfoCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guild = context.event.guild
        val guildId = guild.idLong

        if (context.tokenizer.hasMore) {
            /*
             * you can choose to show info for a user or yourself,
             * this means arguments can be:
             * - @user chara
             * - chara
             */
            val userId = context.tokenizer.nextUserMention() ?: context.event.author.idLong
            val user = context.bot.client.getUserById(userId)

            if (context.tokenizer.hasMore) {
                val name = context.tokenizer.tail()

                if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                    try {
                        val character = context.bot.database.getRoleplayCharacter(guildId, userId, name)
                        if (character != null) {
                            context.replySuccess("here is character info for \"${character.name}\", created by **${guild.getMember(user).effectiveName}** :\n${character.content}")
                        } else {
                            context.replyFail("there was no character with that name.")
                        }
                    } catch (e: SQLException) {
                        LOGGER.error("Could not fetch character info.", e)
                        context.replyFail("an error occurred while fetching character info.")
                    }
                } else {
                    context.replyFail("there was no character with that name.")
                }
            } else {
                context.replyFail("you haven't specified a character name.")
            }
        } else {
            context.replyFail("you haven't specified a character name and/or character owner.")
        }
    }

}
