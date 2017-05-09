package org.kud.roleplay.command.roleplay.characters

class CharaDeleteCommand : org.kud.roleplay.command.meta.command.Command() {

    override fun onInvoke(context: org.kud.roleplay.command.meta.CommandContext) {
        val guildId = context.message.guild.idLong
        val userId = context.message.author.idLong

        if (context.tokenizer.hasMore) {
            val name = context.tokenizer.tail()

            if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    context.bot.database.deleteRoleplayCharacter(guildId, userId, name)
                    context.replySuccess("your character \"$name\" has been deleted!")
                } catch (e: java.sql.SQLException) {
                    org.kud.roleplay.LOGGER.error("Could not delete character.", e)
                    context.replyFail("an error occurred while deleting your character.")
                }
            } else {
                context.replyFail("you have no character with that name.")
            }
        } else {
            context.replyFail("you haven't specified a character name.")
        }
    }

}
