package nuke.roleplaybot.command.roleplay.characters

import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.Command
import nuke.roleplaybot.database.Character
import nuke.roleplaybot.database.Characters
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

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
