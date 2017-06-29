package org.kud.roleplay.bot

import org.kud.roleplay.database.Database
import org.kud.roleplay.music.BotAudioState
import org.kud.roleplay.roleplay.combat.battle.GridBattleManager
import org.kud.roleplay.util.Config

abstract class RoleplayBotBase(override final val config: Config) : RoleplayBot {

    init {
        Database.load(config)
    }

    override final val audio = BotAudioState()
    override final val battles = GridBattleManager()

}