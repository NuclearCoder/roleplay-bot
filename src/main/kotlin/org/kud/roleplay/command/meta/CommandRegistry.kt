package org.kud.roleplay.command.meta

import java.util.*

typealias RegistryBuilderBlock = CommandRegistry.RegistryBuilder.() -> Unit

class CommandRegistry private constructor(private val commands: SortedMap<String, RegisteredCommand>) {

    constructor(builder: RegistryBuilderBlock)
            : this(RegistryBuilder().apply(builder).commands)


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

        internal val commands = sortedMapOf<String, RegisteredCommand>()

        // register final command
        fun register(name: String, command: Command) {
            commands.put(name, RegisteredCommand.Final(name, command))
        }

        // register branch command with a default behaviour
        fun register(name: String, command: Command, subBuilder: RegistryBuilder.() -> Unit) {
            commands.put(name, RegisteredCommand.Branch(name, command, CommandRegistry(subBuilder)))
        }

    }

}