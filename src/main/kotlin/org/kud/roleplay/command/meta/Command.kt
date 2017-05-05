package org.kud.roleplay.command.meta

abstract class Command {
    abstract fun onInvoke(context: CommandContext)
}