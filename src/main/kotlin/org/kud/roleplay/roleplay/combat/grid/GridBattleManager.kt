package org.kud.roleplay.roleplay.combat.grid

import org.kud.roleplay.database.Character
import org.kud.roleplay.roleplay.combat.AbstractBattle
import org.kud.roleplay.roleplay.combat.AbstractBattleManager
import org.kud.roleplay.util.OrderedPair

class GridBattleManager : AbstractBattleManager() {

    override fun newBattleWith(pair: OrderedPair<Character>): AbstractBattle {
        return GridBattle(pair)
    }

}