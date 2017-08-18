package nuke.roleplaybot.roleplay.battle

import nuke.roleplaybot.database.Character
import nuke.roleplaybot.util.OrderedPair

abstract class AbstractBattle(protected val pair: OrderedPair<Character>) {

    operator fun component1() = pair.first
    operator fun component2() = pair.second


}