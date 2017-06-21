package org.kud.roleplay.bot

import org.kud.roleplay.database.Database
import org.kud.roleplay.music.BotAudioState
import org.kud.roleplay.roleplay.combat.grid.GridBattleManager
import org.kud.roleplay.util.Config

abstract class RoleplayBotBase(override final val config: Config) : RoleplayBot {

    override final val database = Database(config)
    override final val audio = BotAudioState()
    override final val battles = GridBattleManager()
}