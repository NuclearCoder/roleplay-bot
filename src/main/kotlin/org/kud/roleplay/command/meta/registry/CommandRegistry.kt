package org.kud.roleplay.command.meta.registry

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

class CommandRegistry private constructor(builder: RegistryBuilder) {

    val fallback = builder.fallback ?: FallbackCommand()
    val commands = builder.commands.apply {
        put("", RegisteredCommand.Final("", fallback))
    }

    constructor(builder: (RegistryBuilder) -> Unit)
            : this(RegistryBuilder().apply(builder))

    fun search(name: String): RegisteredCommand? =
            commands.tailMap(name).let {
                // prefix lookup in sorted map
                tail ->
                if (!tail.isEmpty()) {
                    tail.firstKey().let {
                        if (it.startsWith(name)) tail[it] else null
                    }
                } else null
            }

    class RegistryBuilder internal constructor() {

        internal var fallback: Command? = null
        internal val commands = sortedMapOf<String, RegisteredCommand>()

        // set fallback command
        fun fallback(command: Command?) {
            fallback = command
        }

        // register final command
        fun register(name: String, command: Command) {
            commands[name] = RegisteredCommand.Final(name, command)
        }

        // register branch command with a default behaviour
        fun register(name: String, builder: (RegistryBuilder) -> Unit) {
            commands[name] = CommandRegistry(builder).let {
                RegisteredCommand.Branch(name, it.fallback, it)
            }
        }

        operator fun set(name: String, command: Command) = register(name, command)
        operator fun invoke(name: String, builder: (RegistryBuilder) -> Unit) = register(name, builder)

    }

    inner class FallbackCommand : Command() {

        override fun onInvoke(context: CommandContext) {
            val list = commands.entries
                    .filter { it.key.isNotEmpty() && context.hasSufficientPermission(it.value.command.requiredPermission) }
                    .map { it.key }
                    .joinToString(prefix = "```\n", separator = " | ", postfix = "```")

            context.replyFail("you haven't specified a valid sub-command.\n$list")
        }

    }

}