package org.kud.roleplay

import club.minnced.kjda.client
import club.minnced.kjda.plusAssign
import club.minnced.kjda.token
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.ShutdownEvent
import net.dv8tion.jda.core.hooks.AnnotatedEventManager
import net.dv8tion.jda.core.hooks.SubscribeEvent
import org.kud.roleplay.command.admin.DBCacheClearCommand
import org.kud.roleplay.command.admin.ExitCommand
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.music.MusicStartCommand
import org.kud.roleplay.command.music.MusicStopCommand
import org.kud.roleplay.command.roleplay.characters.CharaCreateCommand
import org.kud.roleplay.command.roleplay.characters.CharaDeleteCommand
import org.kud.roleplay.command.roleplay.characters.CharaInfoCommand
import org.kud.roleplay.command.roleplay.characters.CharaListCommand
import org.kud.roleplay.command.roleplay.characters.CharaUpdateCommand
import org.kud.roleplay.command.roleplay.experience.ExperienceLeaderboardCommand
import org.kud.roleplay.command.roleplay.experience.ExperienceShowCommand
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.database.Database
import org.kud.roleplay.music.BotAudioState
import org.kud.roleplay.roleplay.server.Users
import org.kud.roleplay.util.Config

class RoleplayBot(private val config: Config) {

    val database = Database(config)
    val audio = BotAudioState()
    val users = Users(database)

    val commands = CommandService(this@RoleplayBot) {
        register("exit", ExitCommand())

        register("test", TestCommand())

        register("db") {
            register("clear", DBCacheClearCommand())
        }

        register("music") {
            register("start", MusicStartCommand("https://www.youtube.com/watch?v=zJvhDfYU_LU"))
            register("stop", MusicStopCommand())
        }

        register("chara") {
            register("create", CharaCreateCommand())
            register("delete", CharaDeleteCommand())
            register("update", CharaUpdateCommand())
            register("list", CharaListCommand())
            register("info", CharaInfoCommand())
        }

        register("exp", ExperienceShowCommand())
        register("top", ExperienceLeaderboardCommand())
    }

    val client = client(AccountType.BOT) {
        token { config["token"] }

        setEventManager(AnnotatedEventManager())

        this += object {
            @SubscribeEvent
            fun onReady(event: ReadyEvent) {
                commands.initAfterAttach()
                database.connect()
            }

            @SubscribeEvent
            fun onShutdown(event: ShutdownEvent) {
                database.disconnect()
                config.save()
            }
        }

        this += commands
    }

    fun terminate() {
        LOGGER.info("Waiting for last requests...")

        try {
            //wait for requests for an arbitrary time
            Thread.sleep(SHUTDOWN_WAIT)
        } catch (ignored: InterruptedException) {
        }

        LOGGER.info("Shutting down...")

        client.shutdown()
    }

}
