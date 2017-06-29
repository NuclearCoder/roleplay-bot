package nuke.roleplaybot.bot.sharded

import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.bot.RoleplayBot
import nuke.roleplaybot.bot.buildClient
import nuke.roleplaybot.bot.buildCommands

class RoleplayShard(val bot: RoleplayBot, val shardId: Int, val shardCount: Int) : RoleplayBot by bot {

    init {
        LOGGER.info("Starting shard #$shardId...")
    }

    override val commands = buildCommands(this)
    override val client = buildClient { useSharding(shardId, shardCount) }

}
