package org.kud.roleplay.util

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import org.kud.roleplay.LOGGER
import org.kud.roleplay.URL_GET_TIMEOUT
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