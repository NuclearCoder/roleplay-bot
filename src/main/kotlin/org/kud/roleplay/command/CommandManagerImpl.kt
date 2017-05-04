package org.kud.roleplay.command

import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.manage.ExitCommand
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandManagerImpl(private val bot: RoleplayBot) : CommandManager {

    val prefix = "-rp"

    private val commands: Map<String, Command> = mapOf(
            "exit" to ExitCommand()
    )

    override fun handle(event: MessageReceivedEvent) {
        val message = event.message
        val content = message.content

        if (content.startsWith(prefix)) {
            // drop 1 to get rid of the prefix
            val args = content.split("\\s+").drop(1).dropLastWhile(String::isEmpty).toTypedArray()

            if (args.isEmpty()) {

            } else {
                val name = args.getOrNull(0) ?: ""

                val command = commands[name] ?: return

                command.execute(bot, message, name, args)
            }
        }
    }

}
