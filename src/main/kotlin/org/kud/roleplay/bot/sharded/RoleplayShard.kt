package org.kud.roleplay.bot.sharded

import club.minnced.kjda.client
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.kud.roleplay.bot.RoleplayBot

class RoleplayShard(val bot: RoleplayBotSharded,
                    val shardId: Int, val shardCount: Int,
                    private val builder: JDABuilder.() -> Unit)
    : RoleplayBot by bot {

    private var _client = build()

    override val client get(): JDA = _client

    private fun build() = client(AccountType.BOT) {
        useSharding(shardId, shardCount)
    }

    fun revive() {
        _client.shutdown(false)
        _client = build()
    }

}
