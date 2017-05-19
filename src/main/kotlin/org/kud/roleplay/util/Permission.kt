package org.kud.roleplay.util

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Member
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.PermissionLevel

/**
Created by Chocolate on 5/05/17.

Copyright (c) 2017 Chocolate.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
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