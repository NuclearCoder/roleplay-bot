package org.kud.roleplay.bot.basic

import org.kud.roleplay.LOGGER
import org.kud.roleplay.SHUTDOWN_WAIT
import org.kud.roleplay.bot.RoleplayBotBase
import org.kud.roleplay.bot.buildClient
import org.kud.roleplay.bot.buildCommands
import org.kud.roleplay.util.Config

class RoleplayBotBasic(config: Config) : RoleplayBotBase(config) {
    init {
        LOGGER.info("Starting unsharded bot...")
    }

    override val commands = buildCommands(this)
    override val client = buildClient()

    override fun terminate() {
        LOGGER.info("Waiting for last requests...")

        try {
            //wait for requests for an arbitrary time
            Thread.sleep(SHUTDOWN_WAIT)
        } catch (ignored: InterruptedException) {
        }

        LOGGER.info("Shutting down...")

        client.shutdown()

        config.save()
    }
}