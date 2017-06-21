package org.kud.roleplay.command.test

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

object TestCommand : Command() {
    override fun onInvoke(context: CommandContext) {
        LOGGER.info("Test command.")
        context.reply("<:happuChino:314226131014254592>")
    }
}