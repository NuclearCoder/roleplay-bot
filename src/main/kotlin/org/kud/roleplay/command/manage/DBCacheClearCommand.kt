package org.kud.roleplay.command.manage

import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.Command
import org.kud.roleplay.util.rReply
import sx.blah.discord.handle.obj.IMessage

class DBCacheClearCommand : Command {

    override fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>) {
        if (message.author == message.client.applicationOwner) {
            bot.database.clearCache()
            message.rReply("database cache has been cleared.")
        }
    }

}