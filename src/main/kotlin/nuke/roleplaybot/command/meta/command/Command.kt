package nuke.roleplaybot.command.meta.command

import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.util.hasSufficientPermissions

abstract class Command(val requiredPermission: PermissionLevel = PermissionLevel.User) {

    abstract fun onInvoke(context: CommandContext)

    protected fun CommandContext.hasSufficientPermission(permission: PermissionLevel) =
            message.member.hasSufficientPermissions(this, permission)

}