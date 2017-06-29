package nuke.roleplaybot.bot

import nuke.roleplaybot.database.Database
import nuke.roleplaybot.music.BotAudioState
import nuke.roleplaybot.roleplay.combat.battle.GridBattleManager
import nuke.roleplaybot.util.Config

abstract class RoleplayBotBase(override final val config: Config) : RoleplayBot {

    init {
        Database.load(config)
    }

    override final val audio = BotAudioState()
    override final val battles = GridBattleManager()

}