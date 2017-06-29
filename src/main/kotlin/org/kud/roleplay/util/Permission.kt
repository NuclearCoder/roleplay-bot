package org.kud.roleplay.util

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Member
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.PermissionLevel

fun Member.getPermissionLevel(context: CommandContext): PermissionLevel {
    return when {
        user.id == context.botOwner.id -> PermissionLevel.BotOwner
        isOwner -> PermissionLevel.ServerOwner
        hasPermission(Permission.KICK_MEMBERS) || hasPermission(Permission.BAN_MEMBERS) -> PermissionLevel.Moderator
        context.event.channelType == ChannelType.PRIVATE -> PermissionLevel.Private
        else -> PermissionLevel.User
    }
}

fun Member.hasSufficientPermissions(context: CommandContext, desired: PermissionLevel): Boolean {
    return getPermissionLevel(context) >= desired
}