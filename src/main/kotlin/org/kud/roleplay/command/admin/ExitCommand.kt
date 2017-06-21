package org.kud.roleplay.command.admin

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.OwnerRestrictedCommand

object ExitCommand : OwnerRestrictedCommand() {

    override fun onInvoke(context: CommandContext) {
        context.reply(":wave:", "shutting down...")
        context.bot.terminate()
    }

}