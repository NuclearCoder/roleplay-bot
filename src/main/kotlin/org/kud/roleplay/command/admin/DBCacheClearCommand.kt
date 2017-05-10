package org.kud.roleplay.command.admin

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.OwnerRestrictedCommand

class DBCacheClearCommand : OwnerRestrictedCommand() {
    override fun onInvoke(context: CommandContext) {
        context.bot.database.clearCache()
        context.reply("database cache has been cleared.")
    }

}