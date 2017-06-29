package nuke.roleplaybot.command.test

import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.Command

object TestCommand : Command() {
    override fun onInvoke(context: CommandContext) {
        LOGGER.info("Test command.")
        context.reply("<:happuChino:314226131014254592>")
    }
}