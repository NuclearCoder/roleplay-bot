package org.kud.roleplay.command

import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.manage.DBCacheClearCommand
import org.kud.roleplay.command.manage.ExitCommand
import org.kud.roleplay.command.roleplay.CharaCommand
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.util.hasRoleForGuild
import org.kud.roleplay.util.rReply
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

class CommandManagerImpl(private val bot: RoleplayBot) : CommandManager {

    val prefix = "-rp"

    private val commands: Map<String, Command> = mapOf(
            "exit" to ExitCommand(),
            "dbcache" to DBCacheClearCommand(),
            "chara" to CharaCommand(),
            "test" to TestCommand()
    )

    override fun handle(event: MessageReceivedEvent) {
        val message = event.message
        val content = message.content

        val rawArgs = content.split(' ')
        if (rawArgs.isNotEmpty() && rawArgs[0] == prefix) {
            val args = rawArgs.drop(1).toTypedArray()

            val guild = message.guild
            val author = message.author

            if (author.hasRoleForGuild(guild, bot.database.getRoleplayRoleForGuild(guild.longID))) {
                if (args.isNotEmpty()) {
                    val name = args[0]

                    val command = commands[name]
                    if (command != null) {
                        command.execute(bot, message, name, args)
                    } else {
                        message.rReply("you haven't specified a valid sub-command. :warning: \n```\nchara```")
                    }
                } else {
                    message.rReply("you haven't specified a valid sub-command. :warning: \n```\nchara```")
                }
            } else {
                message.rReply("you don't have the roleplay role for this guild. :warning:")
            }
        }
    }

}
