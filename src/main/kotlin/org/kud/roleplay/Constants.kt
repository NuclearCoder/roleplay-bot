package org.kud.roleplay

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


val LOGGER: Logger = LoggerFactory.getLogger(SimpleBot::class.java)

val GET_TIMEOUT: Int = TimeUnit.SECONDS.toMillis(3).toInt()
val KEEP_ALIVE: Long = TimeUnit.SECONDS.toMillis(5)