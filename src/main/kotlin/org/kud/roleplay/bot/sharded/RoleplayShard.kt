package org.kud.roleplay.bot.sharded

import org.kud.roleplay.LOGGER
import org.kud.roleplay.bot.RoleplayBot
import org.kud.roleplay.bot.buildClient
import org.kud.roleplay.bot.buildCommands

class RoleplayShard(val bot: RoleplayBot, val shardId: Int, val shardCount: Int) : RoleplayBot by bot {

    init {
        LOGGER.info("Starting shard #$shardId...")
    }

    override val commands = buildCommands(this)
    override val client = buildClient { useSharding(shardId, shardCount) }

}
