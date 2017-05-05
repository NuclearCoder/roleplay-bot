package org.kud.roleplay.command.manage

import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext

class ExitCommand : Command() {
    override fun onInvoke(context: CommandContext) {
        context.createResponder().setMessage("shutting down...").setEmote(":wave:").queue()
        context.bot.terminate()
    }
}