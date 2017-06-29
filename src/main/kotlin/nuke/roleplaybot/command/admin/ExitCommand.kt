package nuke.roleplaybot.command.admin

import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.OwnerRestrictedCommand

object ExitCommand : OwnerRestrictedCommand() {

    override fun onInvoke(context: CommandContext) {
        context.reply(":wave:", "shutting down...")
        context.bot.terminate()
    }

}