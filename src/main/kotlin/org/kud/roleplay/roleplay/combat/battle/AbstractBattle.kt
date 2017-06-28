package org.kud.roleplay.roleplay.combat.battle

import org.kud.roleplay.database.Character
import org.kud.roleplay.util.OrderedPair

abstract class AbstractBattle(protected val pair: OrderedPair<Character>) {

    operator fun component1() = pair.first
    operator fun component2() = pair.second


}