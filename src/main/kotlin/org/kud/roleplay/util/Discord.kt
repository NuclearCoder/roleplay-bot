package org.kud.roleplay.util

import org.kud.roleplay.GET_TIMEOUT
import org.kud.roleplay.LOGGER
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.MissingPermissionsException
import sx.blah.discord.util.RequestBuffer
import java.io.IOException
import java.net.URL

fun IChannel.rSendMessage(content: String) {
    RequestBuffer.request {
        try {
            sendMessage(content)
        } catch (e: MissingPermissionsException) {
            LOGGER.error("Could not send message", e)
        } catch (e: DiscordException) {
            LOGGER.error("Could not send message", e)
        }
    }
}

fun IMessage.rReply(content: String) {
    RequestBuffer.request {
        try {
            reply(content)
        } catch (e: MissingPermissionsException) {
            LOGGER.error("Could not reply to message", e)
        } catch (e: DiscordException) {
            LOGGER.error("Could not reply to message", e)
        }
    }
}

fun IChannel.rSendFileURL(content: String, url: String,
                          filename: String) {
    try {
        val conn = URL(url).openConnection()
        conn.doInput = true
        conn.doOutput = true
        conn.readTimeout = GET_TIMEOUT
        conn.connectTimeout = GET_TIMEOUT

        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11")

        conn.inputStream.use {
            RequestBuffer.request {
                try {
                    sendFile(content, false, it, filename)
                } catch (e: MissingPermissionsException) {
                    LOGGER.error("Could not send file", e)
                } catch (e: DiscordException) {
                    LOGGER.error("Could not send file", e)
                }
            }
        }
    } catch (e: IOException) {
        LOGGER.error("Could not send file:", e)
    }

}