package org.kud.roleplay

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

val LOGGER: Logger = LoggerFactory.getLogger(RoleplayBot::class.java)

val URL_GET_TIMEOUT = TimeUnit.SECONDS.toMillis(3).toInt()
val SHUTDOWN_WAIT = TimeUnit.SECONDS.toMillis(3)

val CONFIG_FILENAME = "./roleplaybot.cfg"
val CONFIG_AUTOSAVE_PERIOD = TimeUnit.MINUTES.toMillis(15)

val DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver"
val DATABASE_CACHE_TIMEOUT = TimeUnit.MINUTES.toMillis(5)
val DATABASE_CACHE_SWEEP_PERIOD = TimeUnit.MINUTES.toMillis(20)
val DATABASE_CACHE_THRESHOLD = 20L