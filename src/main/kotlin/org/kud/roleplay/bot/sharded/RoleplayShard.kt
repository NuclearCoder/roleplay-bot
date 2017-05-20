package org.kud.roleplay.bot.sharded

import org.kud.roleplay.LOGGER
import org.kud.roleplay.bot.RoleplayBot
import org.kud.roleplay.bot.buildClient
import org.kud.roleplay.bot.buildCommands

class RoleplayShard(val bot: RoleplayBotSharded, val shardId: Int, val shardCount: Int) : RoleplayBot {

    init {
        LOGGER.info("Starting shard #$shardId...")
    }

    override val config get() = bot.config
    override val database get() = bot.database
    override val audio get() = bot.audio

    override val commands = buildCommands(this)
    override val client = buildClient { useSharding(shardId, shardCount) }

    override fun terminate() = bot.terminate()

}
