package nuke.roleplaybot.command.meta

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.SubscribeEvent
import nuke.roleplaybot.bot.RoleplayBot
import nuke.roleplaybot.command.meta.command.Command
import nuke.roleplaybot.command.meta.registry.CommandRegistry
import nuke.roleplaybot.command.meta.registry.RegisteredCommand
import nuke.roleplaybot.roleplay.message.MessageHandler
import nuke.roleplaybot.util.MessageTokenizer
import nuke.roleplaybot.util.hasSufficientPermissions

class CommandService(private val bot: RoleplayBot,
                     commandBuilder: (CommandRegistry.RegistryBuilder) -> Unit) {

    companion object {
        const val prefix = "rp!"
    }

    private val owner by lazy {
        bot.client.asBot().applicationInfo.complete().owner
    }

    private val registry = CommandRegistry(commandBuilder)

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
                tokenizer.nextWord().takeIf(String::isNotEmpty)?.let { name ->
                    processCommand(event, tokenizer, name, registry)
                }
            } else { // is a message
                MessageHandler.processMessage(event)
            }
        }
    }

}
