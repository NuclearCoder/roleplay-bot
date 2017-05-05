package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.util.hasRoleForGuild
import org.kud.roleplay.util.userHasSufficientPermissions

class CommandService(private val bot: RoleplayBot) : ListenerAdapter() {

    var cmdPref = "-"
    var rootCmd = "rp"

    val prefix = "$cmdPref$rootCmd"
    lateinit var owner: User

    private var registry: ArrayList<RegisteredCommand> = ArrayList()

    fun initAfterAttach() {
        bot.client.asBot().applicationInfo.queue({
            owner = it.owner
        })
    }

    fun search(name: String): RegisteredCommand? {
        return registry.find { command -> command.name == name }
    }

    fun register(name: String, command: Command) {
        registry.add(RegisteredCommand(name, command))
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
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
                val command = search(name)

                if (command == null) {
                    context.createResponder().fail().setMessage("you haven't specified a valid roleplay command.").queue()
                    return
                }

                if (author.hasRoleForGuild(bot.database.getRoleplayRoleForGuild(guild.idLong))) {
                    if (userHasSufficientPermissions(author, owner, context, command.command.requiredPermission)) {
                        command.command.onInvoke(context)
                    } else {
                        context.createResponder().fail().setMessage("this command requires the `${command.command.requiredPermission.name}` permission.").queue()
                    }
                } else {
                    context.createResponder().fail().setMessage("you don't have the roleplay role for this guild.").queue()
                }
            }
        }
    }

}
