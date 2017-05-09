package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.command.meta.registry.CommandRegistry
import org.kud.roleplay.command.meta.registry.RegisteredCommand
import org.kud.roleplay.roleplay.message.MessageHandler
import org.kud.roleplay.util.MessageTokenizer
import org.kud.roleplay.util.hasSufficientPermissions

class CommandService(private val bot: RoleplayBot,
                     commandBuilder: CommandRegistry.RegistryBuilder.() -> Unit) {

    companion object {
        const val cmdPref = "-"
        const val rootCmd = "rp"

        const val prefix = cmdPref + rootCmd
    }

    lateinit var owner: User

    private val registry = CommandRegistry(commandBuilder)
    private val messageHandler = MessageHandler()

    fun initAfterAttach() {
        bot.client.asBot().applicationInfo.queue {
            owner = it.owner
        }
    }

    private tailrec fun processCommand(event: MessageReceivedEvent, tokenizer: MessageTokenizer,
                                       name: String, registry: CommandRegistry) {
        val command = registry.search(name)
        when (command) {
            is RegisteredCommand.Branch -> processCommand(event, tokenizer, tokenizer.nextWord(), command.registry)
            is RegisteredCommand.Final -> command.command.call(CommandContext(event, bot, event.message, name, tokenizer))
            else -> { /* command == null */
                registry.fallback.call(CommandContext(event, bot, event.message, "", tokenizer))
            }
        }
    }

    private fun Command.call(context: CommandContext) {
        if (context.event.member.hasSufficientPermissions(owner, context, this.requiredPermission)) {
            this.onInvoke(context)
        } else {
            context.replyFail("this command requires the `${this.requiredPermission.name}` permission.")
        }
    }

    @SubscribeEvent
    fun onMessage(event: MessageReceivedEvent) {
        val tokenizer = MessageTokenizer(event.message.content)

        if (tokenizer.skip(prefix)) { // is a command
            processCommand(event, tokenizer, tokenizer.nextWord(), registry)
        } else { // is a message
            messageHandler.processMessage(event, bot)
        }
    }

}
