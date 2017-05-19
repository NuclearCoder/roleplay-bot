package org.kud.roleplay.command.roleplay.characters

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Character

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

                if (Character.exists(guildId, userId, name)) {
                    val character = Character.find(Character.equals(guildId, userId, name)).first()

                    context.reply("here is character info for \"${character.name}\", created by **${guild.getMember(user).effectiveName}** :\n${character.content}")
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
