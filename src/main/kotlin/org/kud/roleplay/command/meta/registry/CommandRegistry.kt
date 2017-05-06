package org.kud.roleplay.command.meta.registry

import org.kud.roleplay.command.meta.command.Command

class CommandRegistry private constructor(builder: RegistryBuilder) {

    val commands = builder.commands
    val fallback = builder.fallback

    constructor(builder: RegistryBuilder.() -> Unit)
            : this(RegistryBuilder().apply(builder))

    fun search(name: String): RegisteredCommand? {
        // prefix lookup in sorted map
        val tail = commands.tailMap(name)
        return if (!tail.isEmpty()) {
            tail.firstKey().let {
                if (it.startsWith(name)) tail[it] else null
            }
        } else null
    }

    class RegistryBuilder internal constructor() {

        internal var fallback: Command = Command.Pass
        internal val commands = sortedMapOf<String, RegisteredCommand>()

        // set fallback command
        fun fallback(command: Command) {
            fallback = command
        }

        // register final command
        fun register(name: String, command: Command) {
            commands[name] = RegisteredCommand.Final(name, command)
        }

        // register branch command with a default behaviour
        fun register(name: String, subBuilder: RegistryBuilder.() -> Unit) {
            commands[name] = CommandRegistry(subBuilder).let {
                RegisteredCommand.Branch(name, it.fallback, it)
            }
        }

    }

}