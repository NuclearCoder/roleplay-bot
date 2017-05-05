package org.kud.roleplay.command.test

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext

class TestCommand : Command() {
    override fun onInvoke(context: CommandContext) {
        LOGGER.info("Test command.")
        context.createResponder().setMessage("test!").queue()
    }
}