package org.kud.roleplay.command.roleplay.characters

class CharaListCommand : org.kud.roleplay.command.meta.command.Command() {

    override fun onInvoke(context: org.kud.roleplay.command.meta.CommandContext) {
        val guildId = context.event.guild.idLong

        val userId = if (context.tokenizer.hasMore) {
            context.tokenizer.nextUserMention()
        } else {
            context.event.author.idLong
        }

        if (userId != null) {
            val characters = context.bot.database.getRoleplayCharacterList(guildId, userId)

            if (characters.isNotEmpty()) {
                val list = characters.map { it.name }.joinToString(prefix = "```\n", separator = "\n", postfix = "```")
                context.replySuccess("here are the characters created by this user.\n$list")
            } else {
                context.replyFail("this user has no character.")
            }
        } else {
            context.replyFail("this is not a valid mention.")
        }
    }

}
