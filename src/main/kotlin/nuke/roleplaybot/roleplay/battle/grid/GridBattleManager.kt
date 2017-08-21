package nuke.roleplaybot.roleplay.battle.grid

import net.dv8tion.jda.core.entities.TextChannel
import nuke.roleplaybot.database.Character
import nuke.roleplaybot.roleplay.battle.AbstractBattleManager
import nuke.roleplaybot.util.OrderedPair

class GridBattleManager : AbstractBattleManager() {

    override fun newBattleWith(channel: TextChannel, pair: OrderedPair<Character>) = GridBattle(channel, pair)

}