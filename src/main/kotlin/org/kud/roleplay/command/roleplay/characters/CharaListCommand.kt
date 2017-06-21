package org.kud.roleplay.command.roleplay.characters

import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Characters

object CharaListCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong

        val userId = if (context.tokenizer.hasMore) {
            context.tokenizer.nextUserMention()
        } else {
            context.event.author.idLong
        }

        if (userId != null) {
            transaction {
                val characters = Characters.select {
                    (Characters.guildId eq guildId) and (Characters.userId eq userId)
                }

                if (!characters.empty()) {
                    val list = characters.map { it[Characters.name] }.joinToString(prefix = "```\n", separator = "\n", postfix = "```")
                    context.reply("here are the characters created by this user.\n$list")
                } else {
                    context.replyFail("this user has no character.")
                }
            }
        } else {
            context.replyFail("this is not a valid mention.")
        }
    }

}
