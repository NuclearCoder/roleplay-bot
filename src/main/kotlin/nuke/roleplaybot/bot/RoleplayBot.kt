package nuke.roleplaybot.bot

import net.dv8tion.jda.core.JDA
import nuke.roleplaybot.command.meta.CommandService
import nuke.roleplaybot.music.BotAudioState
import nuke.roleplaybot.roleplay.combat.battle.AbstractBattleManager
import nuke.roleplaybot.util.Config

interface RoleplayBot {

    val config: Config
    val client: JDA

    val commands: CommandService
    val audio: BotAudioState
    val battles: AbstractBattleManager

    fun terminate()

}