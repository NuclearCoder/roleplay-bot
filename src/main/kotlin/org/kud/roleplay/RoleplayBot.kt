package org.kud.roleplay

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.kud.roleplay.command.manage.DBCacheClearCommand
import org.kud.roleplay.command.manage.MusicEndCommand
import org.kud.roleplay.command.manage.MusicStartCommand
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.database.Database
import org.kud.roleplay.util.Config

class RoleplayBot(private val config: Config) {

    private val keeper = TimerKeepAlive()

    var commands = CommandService(this)

    val client: JDA = JDABuilder(AccountType.BOT).setToken(config["token"]).buildBlocking()

    init {
        // Register commands here
        commands.register("test", TestCommand())
        commands.register("dbclear", DBCacheClearCommand())
        commands.register("musicstart", MusicStartCommand("https://www.youtube.com/watch?v=zJvhDfYU_LU"))
        commands.register("musicend", MusicEndCommand())
        client.addEventListener(commands)
        commands.initAfterAttach()
    }

    val database = Database(config)

    fun terminate() {
        LOGGER.info("Shutting down...")
        client.shutdown()
        database.disconnect()
        config.save()
        keeper.alive.set(false)
    }

}
