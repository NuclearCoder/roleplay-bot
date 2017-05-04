package org.kud.roleplay.command.manage

import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.Command
import org.kud.roleplay.util.rSendMessage
import sx.blah.discord.handle.obj.IMessage

class ExitCommand : Command {

    override fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>) {
        message.channel.rSendMessage(":wave:")
        bot.terminate()
    }

}