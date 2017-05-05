package org.kud.roleplay.command.manage

import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.PermissionLevel

class ExitCommand : Command(PermissionLevel.BotOwner) {

    override fun onInvoke(context: CommandContext) {
        context.reply {
            setMessage("shutting down...")
            setEmote(":wave:")
        }
        context.bot.terminate()
    }

}