package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.EventListener
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.util.hasSufficientPermissions

class CommandService(private val bot: RoleplayBot, commandBuilder: RegistryBuilderBlock) : EventListener {

    companion object {
        const val cmdPref = "-"
        const val rootCmd = "rp"

        const val prefix = "$cmdPref$rootCmd"
    }

    lateinit var owner: User

    private val registry = CommandRegistry(commandBuilder)

    fun initAfterAttach() {
        bot.client.asBot().applicationInfo.queue {
            owner = it.owner
        }
    }

    override fun onEvent(event: Event) {
        if (event !is MessageReceivedEvent) return

        val message = event.message
        val content = message.content

        val rawArgs = content.split(' ')
        if (rawArgs.isNotEmpty() && rawArgs[0] == prefix) {
            val args = rawArgs.drop(1).toTypedArray()

            val guild = message.guild
            val author = message.member

            if (args.isNotEmpty()) {
                val name = args[0]
                val context = CommandContext(event, bot, message, name, args)
                val command = registry.search(name) // TODO: handle subcommands

                if (command == null) {
                    context.reply {
                        fail()
                        setMessage("you haven't specified a valid roleplay command.")
                    }
                    return
                }

                /*if (author.hasRoleForGuild(bot.database.getRoleplayRoleForGuild(guild.idLong))) {*/
                    if (author.hasSufficientPermissions(owner, context, command.command.requiredPermission)) {
                        command.command.onInvoke(context)
                    } else {
                        context.reply {
                            fail()
                            setMessage("this command requires the `${command.command.requiredPermission.name}` permission.")
                        }
                    }
                /*} else {
                    context.reply {
                        fail()
                        setMessage("you don't have the roleplay role for this guild.")
                    }
                }*/
            }
        }
    }

}
