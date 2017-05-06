package org.kud.roleplay.command.admin

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.OwnerRestrictedCommand

class ExitCommand : OwnerRestrictedCommand() {

    override fun onInvoke(context: CommandContext) {
        context.reply {
            setMessage("shutting down...")
            setEmote(":wave:")
        }
        context.bot.terminate()
    }

}