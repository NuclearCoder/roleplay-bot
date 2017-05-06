package org.kud.roleplay

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.kud.roleplay.command.admin.DBCacheClearCommand
import org.kud.roleplay.command.admin.ExitCommand
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.music.MusicEndCommand
import org.kud.roleplay.command.music.MusicStartCommand
import org.kud.roleplay.command.roleplay.*
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.database.Database
import org.kud.roleplay.music.BotAudioState
import org.kud.roleplay.util.Config

class RoleplayBot(private val config: Config) {

    private val keeper = TimerKeepAlive()

    val client: JDA = JDABuilder(AccountType.BOT).setToken(config["token"]).buildBlocking()

    val commands = CommandService(this) {
        register("exit", ExitCommand())

        register("test", TestCommand())

        register("db") {
            register("clear", DBCacheClearCommand())
        }

        register("music") {
            register("start", MusicStartCommand("https://www.youtube.com/watch?v=zJvhDfYU_LU"))
            register("end", MusicEndCommand())
        }

        register("chara") {
            fallback(CharaCommand())
            register("create", CharaCreateCommand())
            register("delete", CharaDeleteCommand())
            register("update", CharaUpdateCommand())
            register("list", CharaListCommand())
            register("info", CharaInfoCommand())
        }
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
