package org.kud.roleplay.command

import org.kud.roleplay.SimpleBot
import org.kud.roleplay.command.manage.ExitCommand
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandManagerImpl(private val bot: SimpleBot) : CommandManager {

    val prefix = "hey nuke, "

    private val commands: Map<String, Command> = mapOf(
            "exit" to ExitCommand()
    )

    override fun handle(event: MessageReceivedEvent) {
        val message = event.message
        val content = message.content

        if (content.startsWith(prefix)) {
            val args = content.substring(prefix.length).split(' ').dropLastWhile(String::isEmpty).toTypedArray()
            val name = args[0]

            val command = commands[name] ?: return

            command.execute(bot, message, name, args)
        }
    }

}
