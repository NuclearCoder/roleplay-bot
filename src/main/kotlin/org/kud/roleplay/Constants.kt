package org.kud.roleplay

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

val LOGGER: Logger = LoggerFactory.getLogger(RoleplayBot::class.java)

val URL_GET_TIMEOUT: Int = TimeUnit.SECONDS.toMillis(3).toInt()
val BOT_KEEP_ALIVE: Long = TimeUnit.SECONDS.toMillis(5)

val CONFIG_FILENAME: String = "nukebot.cfg"
val CONFIG_AUTOSAVE_PERIOD: Long = TimeUnit.MINUTES.toMillis(15)

val DATABASE_DRIVER = "com.mysql.jdbc.Driver"