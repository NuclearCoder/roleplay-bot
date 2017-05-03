package org.kud.roleplay.command

import org.kud.roleplay.SimpleBot
import sx.blah.discord.handle.obj.IMessage

@FunctionalInterface interface Command {

    fun execute(bot: SimpleBot, message: IMessage, command: String, args: Array<String>)

}

fun (() -> Unit).toCommand(): Command {
    return object : Command {
        override fun execute(bot: SimpleBot, message: IMessage, command: String, args: Array<String>) {
            this@toCommand.invoke()
        }
    }
}
