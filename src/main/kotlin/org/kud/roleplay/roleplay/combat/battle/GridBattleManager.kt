package org.kud.roleplay.roleplay.combat.battle

import org.kud.roleplay.database.Character
import org.kud.roleplay.roleplay.combat.battle.grid.GridBattle
import org.kud.roleplay.util.OrderedPair

class GridBattleManager : AbstractBattleManager() {

    override fun newBattleWith(pair: OrderedPair<Character>): AbstractBattle {
        return GridBattle(pair)
    }

}