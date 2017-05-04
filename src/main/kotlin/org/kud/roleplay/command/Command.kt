package org.kud.roleplay.command

import org.kud.roleplay.RoleplayBot
import sx.blah.discord.handle.obj.IMessage

@FunctionalInterface interface Command {

    fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>)

}

fun (() -> Unit).toCommand(): Command {
    return object : Command {
        override fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>) {
            this@toCommand.invoke()
        }
    }
}
