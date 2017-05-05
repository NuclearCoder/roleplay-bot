package org.kud.roleplay.command.manage

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.OwnerRestrictedCommand

class DBCacheClearCommand : OwnerRestrictedCommand() {
    override fun onInvoke(context: CommandContext) {
        context.bot.database.clearCache()
        context.reply {
            setMessage("database cache has been cleared.")
            success()
        }
    }

}