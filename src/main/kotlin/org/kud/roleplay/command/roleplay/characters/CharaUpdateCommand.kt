package org.kud.roleplay.command.roleplay.characters

import org.jetbrains.exposed.sql.update
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Character
import org.kud.roleplay.database.Characters

class CharaUpdateCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = context.event.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tailUntil { it == '\n' }

            if (Character.exists(guildId, userId, name)) {
                if (context.tokenizer.hasMore) {
                    val content = context.tokenizer.tail()

                    // TODO: maybe sanity check on content

                    Characters.update(Character.eqOp(guildId, userId, name)) {
                        it[Characters.content] = content
                    }

                    context.reply("your character \"$name\" has been updated!")
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
