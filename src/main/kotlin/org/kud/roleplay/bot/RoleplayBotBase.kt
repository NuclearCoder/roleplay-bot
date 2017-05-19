package org.kud.roleplay.bot

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
import org.kud.roleplay.util.Config

abstract class RoleplayBotBase(override final val config: Config) : RoleplayBot {

    override final val database = Database(config)
    override final val audio = BotAudioState()

    fun buildCommands(instance: RoleplayBot) = CommandService(instance) {
        register("exit", ExitCommand())
        register("test", TestCommand())

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

}