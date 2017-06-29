package nuke.roleplaybot.roleplay.combat.battle

import nuke.roleplaybot.database.Character
import nuke.roleplaybot.roleplay.combat.battle.grid.GridBattle
import nuke.roleplaybot.util.OrderedPair

class GridBattleManager : AbstractBattleManager() {

    override fun newBattleWith(pair: OrderedPair<Character>): AbstractBattle {
        return GridBattle(pair)
    }

}