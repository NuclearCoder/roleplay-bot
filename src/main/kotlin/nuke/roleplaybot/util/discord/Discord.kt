package nuke.roleplaybot.util.discord

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.URL_GET_TIMEOUT
import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.PermissionLevel
import nuke.roleplaybot.util.use
import java.io.IOException
import java.io.InputStream
import java.net.URL

fun TextChannel.sendImagefromURL(content: String, url: String,
                                 filename: String) {
    try {
        getInputStreamFromUrl(url).use {
            this.sendFile(it, filename, MessageBuilder().append(content).build()).queue()
        }
    } catch (e: IOException) {
        LOGGER.error("Could not send file:", e)
    }

}

fun getInputStreamFromUrl(url: String): InputStream {
    val conn = URL(url).openConnection()
    conn.doInput = true
    conn.doOutput = true
    conn.readTimeout = URL_GET_TIMEOUT
    conn.connectTimeout = URL_GET_TIMEOUT

    conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")

    return conn.getInputStream()
}

fun Member.hasRoleForGuild(roleID: Long): Boolean {
    return this.roles.any { it.idLong == roleID }
}

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

typealias RMCallback = ReactionMenu.(Member) -> Unit