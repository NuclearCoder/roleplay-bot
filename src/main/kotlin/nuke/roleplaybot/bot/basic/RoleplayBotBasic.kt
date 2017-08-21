package nuke.roleplaybot.bot.basic

import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.bot.RoleplayBotBase
import nuke.roleplaybot.bot.buildClient
import nuke.roleplaybot.bot.buildCommands
import nuke.roleplaybot.util.Config

class RoleplayBotBasic(config: Config) : RoleplayBotBase(config) {
    init {
        LOGGER.info("Starting unsharded bot...")
    }

    override val commands = buildCommands(this)
    override val client = buildClient()

    override fun terminate() {
        LOGGER.info("Shutting down...")

        client.shutdown()

        config.save()
    }
}