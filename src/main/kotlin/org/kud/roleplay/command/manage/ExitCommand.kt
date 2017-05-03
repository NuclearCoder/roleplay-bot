package org.kud.roleplay.command.manage

import org.kud.roleplay.SimpleBot
import org.kud.roleplay.command.Command
import org.kud.roleplay.util.rSendMessage
import sx.blah.discord.handle.obj.IMessage

/**
 * Created by NuclearCoder on 06/02/2017.
 */
class ExitCommand : Command {

    override fun execute(bot: SimpleBot, message: IMessage, command: String, args: Array<String>) {
        message.channel.rSendMessage(":wave:")
        bot.terminate()
    }

}