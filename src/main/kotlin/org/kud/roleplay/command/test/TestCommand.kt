package org.kud.roleplay.command.test

import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.Command
import sx.blah.discord.handle.obj.IMessage

class TestCommand : Command {

    override fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>) {
        //LOGGER.info("message: ${message.content.split('\n').joinToString(prefix = "[", separator = ", ", postfix = "]")}")
    }

}