package nuke.roleplaybot.bot.sharded

import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.core.JDA
import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.SHUTDOWN_WAIT
import nuke.roleplaybot.bot.RoleplayBotBase
import nuke.roleplaybot.bot.getRecommendedShardCount
import nuke.roleplaybot.command.meta.CommandService
import nuke.roleplaybot.util.Config
import java.io.IOException

class RoleplayBotSharded(config: Config, shardCount: Int = 0) : RoleplayBotBase(config) {

    init {
        LOGGER.info("Starting sharded bot...")
    }

    private val shardCount = if (shardCount > 0) shardCount else getRecommendedShardCount()
    private val shards = Array(this.shardCount) { RoleplayShard(this, it, this.shardCount) }

    override val commands: CommandService
        get() = error("Shard manager does not have a command service of its own")

    override val client: JDA
        get() = error("Shard manager does not have a client of its own")

    override fun terminate() {
        LOGGER.info("Waiting for last requests...")

        try {
            //wait for requests for an arbitrary time
            Thread.sleep(SHUTDOWN_WAIT)
        } catch (ignored: InterruptedException) {
        }

        shards.forEach {
            LOGGER.info("Shutting down shard #${it.shardId}...")
            it.client.shutdown(false)
        }

        try {
            Unirest.shutdown()
        } catch (ignored: IOException) {
        }

        config.save()
    }

}
