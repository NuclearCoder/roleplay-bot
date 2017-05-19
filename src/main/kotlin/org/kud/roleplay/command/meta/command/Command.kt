package org.kud.roleplay.command.meta.command

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.util.hasSufficientPermissions

abstract class Command(val requiredPermission: PermissionLevel = PermissionLevel.User) {

    abstract fun onInvoke(context: CommandContext)

    protected fun CommandContext.hasSufficientPermission(permission: PermissionLevel) =
            message.member.hasSufficientPermissions(this, permission)

}