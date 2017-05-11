package org.kud.roleplay.command.meta.command

import org.kud.roleplay.command.meta.CommandContext

abstract class Command(val requiredPermission: PermissionLevel = PermissionLevel.User) {

    abstract fun onInvoke(context: CommandContext)

}