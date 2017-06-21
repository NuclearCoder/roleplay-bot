package org.kud.roleplay.command.roleplay.characters

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Character
import org.kud.roleplay.database.Characters

object CharaDeleteCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = context.event.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tail()

            transaction {
                if (Character.exists(guildId, userId, name)) {
                    Characters.deleteWhere(Character.eqOp(guildId, userId, name))

                    context.reply("your character \"$name\" has been deleted!")
                } else {
                    context.replyFail("you have no character with that name.")
                }
            }
        } else {
            context.replyFail("you haven't specified a character name.")
        }
    }

}
