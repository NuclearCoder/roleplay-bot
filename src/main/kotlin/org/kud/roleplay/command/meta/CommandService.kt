package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent
import org.kud.roleplay.bot.RoleplayBot
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.command.meta.registry.CommandRegistry
import org.kud.roleplay.command.meta.registry.RegisteredCommand
import org.kud.roleplay.roleplay.message.MessageHandler
import org.kud.roleplay.util.MessageTokenizer
import org.kud.roleplay.util.hasSufficientPermissions

class CommandService(private val bot: RoleplayBot,
                     commandBuilder: (CommandRegistry.RegistryBuilder) -> Unit) {

    companion object {
        /*const val cmdPref = "-"
        const val rootCmd = "rp"

        const val prefix = cmdPref + rootCmd*/

        const val prefix = "rp!"
    }

    private var _owner: User? = null
    private val owner get(): User = _owner ?: throw UninitializedPropertyAccessException("Owner has not been set")

    private val registry = CommandRegistry(commandBuilder)
    private val messageHandler = MessageHandler()

    @SubscribeEvent
    fun onReady(event: ReadyEvent) {
        bot.client.asBot().applicationInfo.queue {
            _owner = it.owner
        }

        // block until owner is set
        while (_owner == null) {
            try {
                Thread.sleep(100)
            } catch (ignored: InterruptedException) {
            }
        }
    }

    private tailrec fun processCommand(event: MessageReceivedEvent, tokenizer: MessageTokenizer,
                                       name: String, registry: CommandRegistry) {
        val command = registry.search(name)
        when (command) {
            is RegisteredCommand.Branch -> processCommand(event, tokenizer, tokenizer.nextWord(), command.registry)
            is RegisteredCommand.Final -> command.command.call(CommandContext(event, bot, owner, event.message, name, tokenizer))
            else -> registry.fallback.call(CommandContext(event, bot, owner, event.message, "", tokenizer))
        }
    }

    private fun Command.call(context: CommandContext) {
        if (context.event.member.hasSufficientPermissions(context, this.requiredPermission)) {
            this.onInvoke(context)
        } else {
            context.replyFail("this command requires the `${this.requiredPermission.name}` permission.")
        }
    }

    @SubscribeEvent
    fun onMessage(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val tokenizer = MessageTokenizer(event.message.rawContent)
        if (tokenizer.hasMore) {
            if (tokenizer.skip(prefix)) { // is a command
                processCommand(event, tokenizer, tokenizer.nextWord(), registry)
            } else { // is a message
                messageHandler.processMessage(event)
            }
        }
    }

}
