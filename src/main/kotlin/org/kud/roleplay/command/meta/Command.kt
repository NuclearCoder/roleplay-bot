package org.kud.roleplay.command.meta

abstract class Command(val requiredPermission: PermissionLevel = PermissionLevel.User) {

    abstract fun onInvoke(context: CommandContext)

}