package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.EventListener
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.command.meta.registry.CommandRegistry
import org.kud.roleplay.command.meta.registry.RegisteredCommand
import org.kud.roleplay.util.MessageTokenizer
import org.kud.roleplay.util.hasSufficientPermissions

class CommandService(private val bot: RoleplayBot, commandBuilder: CommandRegistry.RegistryBuilder.() -> Unit) : EventListener {

    companion object {
        const val cmdPref = "-"
        const val rootCmd = "rp"

        const val prefix = cmdPref + rootCmd
    }

    lateinit var owner: User

    private val registry = CommandRegistry(commandBuilder)

    fun initAfterAttach() {
        bot.client.asBot().applicationInfo.queue {
            owner = it.owner
        }
    }

    private fun processCommand(event: MessageReceivedEvent, tokenizer: MessageTokenizer, name: String, registry: CommandRegistry) {
        val command = registry.search(name)
        if (command is RegisteredCommand.Branch) {
            processCommand(event, tokenizer, tokenizer.nextWord(), command.registry)
        } else if (command is RegisteredCommand.Final) {
            command.command.call(CommandContext(event, bot, event.message, name, tokenizer))
        } else { /* command == null */
            registry.fallback.call(CommandContext(event, bot, event.message, "", tokenizer))
        }
    }

    private fun Command.call(context: CommandContext) {
        if (context.event.member.hasSufficientPermissions(owner, context, this.requiredPermission)) {
            this.onInvoke(context)
        } else {
            context.replyFail("this command requires the `${this.requiredPermission.name}` permission.")
        }
    }

    override fun onEvent(event: Event) {
        if (event !is MessageReceivedEvent) return

        val message = event.message
        val content = message.content

        val tokenizer = MessageTokenizer(content)

        if (tokenizer.skip(prefix)) {
            processCommand(event, tokenizer, tokenizer.nextWord(), registry)
        }
    }

}
