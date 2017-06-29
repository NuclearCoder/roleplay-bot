package nuke.roleplaybot

import nuke.roleplaybot.bot.RoleplayBot
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

val LOGGER: Logger = LoggerFactory.getLogger(RoleplayBot::class.java)

val URL_GET_TIMEOUT = TimeUnit.SECONDS.toMillis(3).toInt()
val SHUTDOWN_WAIT = TimeUnit.SECONDS.toMillis(3)

val CONFIG_FILENAME = "./roleplaybot.cfg"
val CONFIG_AUTOSAVE_PERIOD = TimeUnit.MINUTES.toMillis(15)