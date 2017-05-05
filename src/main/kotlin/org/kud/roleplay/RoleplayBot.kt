package org.kud.roleplay

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.kud.roleplay.command.manage.DBCacheClearCommand
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.music.BotAudioState
import org.kud.roleplay.command.music.MusicEndCommand
import org.kud.roleplay.command.music.MusicStartCommand
import org.kud.roleplay.command.roleplay.CharaCommand
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.database.Database
import org.kud.roleplay.util.Config

class RoleplayBot(private val config: Config) {

    private val keeper = TimerKeepAlive()

    val client: JDA = JDABuilder(AccountType.BOT).setToken(config["token"]).buildBlocking()


    val commands = CommandService(this).apply {
        register("test", TestCommand())
        register("dbclear", DBCacheClearCommand())
        register("musicstart", MusicStartCommand("https://www.youtube.com/watch?v=zJvhDfYU_LU"))
        register("musicend", MusicEndCommand())
        register("chara", CharaCommand())
    }
    val database = Database(config)
    val audio = BotAudioState()

    init {
        client.addEventListener(commands)
        commands.initAfterAttach()
    }

    fun terminate() {
        LOGGER.info("Shutting down...")
        client.shutdown()
        database.disconnect()
        config.save()
        keeper.alive.set(false)
    }

}
