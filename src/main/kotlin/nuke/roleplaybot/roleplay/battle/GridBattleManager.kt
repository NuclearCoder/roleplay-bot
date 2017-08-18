package nuke.roleplaybot.roleplay.battle

import nuke.roleplaybot.database.Character
import nuke.roleplaybot.roleplay.battle.grid.GridBattle
import nuke.roleplaybot.util.OrderedPair

class GridBattleManager : AbstractBattleManager() {

    override fun newBattleWith(pair: OrderedPair<Character>) = GridBattle(pair)

}