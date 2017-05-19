package org.kud.roleplay.bot.sharded

import net.dv8tion.jda.core.JDA
import org.kud.roleplay.bot.RoleplayBotBase
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.util.Config

class RoleplayBotSharded(config: Config) : RoleplayBotBase(config) {

    private val shards = arrayListOf<RoleplayShard>()

    override val commands: CommandService
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val client: JDA
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun terminate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}