package org.kud.roleplay.command.roleplay.characters

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Character
import org.kud.roleplay.database.User
import org.kud.roleplay.database.Users

object CharaSelectCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = context.event.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tailUntil { it == '\n' }

            if (Character.exists(guildId, userId, name)) {
                transaction {
                    Users.update(User.eqOp(guildId, userId)) {
                        it[Users.activeCharacter] = name
                    }
                }
                context.reply("your active character is now \"$name\".")
            } else {
                context.replyFail("you have no character with that name.")
            }
        } else {
            context.replyFail("you haven't specified a character name.")
        }
    }

}
