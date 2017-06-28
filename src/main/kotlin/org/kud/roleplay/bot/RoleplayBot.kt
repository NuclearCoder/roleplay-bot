package org.kud.roleplay.bot

import net.dv8tion.jda.core.JDA
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.database.Database
import org.kud.roleplay.music.BotAudioState
import org.kud.roleplay.roleplay.combat.battle.AbstractBattleManager
import org.kud.roleplay.util.Config

interface RoleplayBot {

    val config: Config
    val database: Database
    val client: JDA

    val commands: CommandService
    val audio: BotAudioState
    val battles: AbstractBattleManager

    fun terminate()

}