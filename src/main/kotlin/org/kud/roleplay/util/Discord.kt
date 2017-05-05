package org.kud.roleplay.util

import net.dv8tion.jda.core.entities.Member

/*fun IChannel.rSendFileURL(content: String, url: String,
                          filename: String) {
    try {
        val conn = URL(url).openConnection()
        conn.doInput = true
        conn.doOutput = true
        conn.readTimeout = URL_GET_TIMEOUT
        conn.connectTimeout = URL_GET_TIMEOUT

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
*/
fun Member.hasRoleForGuild(roleID: Long): Boolean {
    return this.roles.any { it.idLong == roleID }
}