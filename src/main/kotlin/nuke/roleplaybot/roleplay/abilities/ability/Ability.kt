package nuke.roleplaybot.roleplay.abilities.ability

import nuke.roleplaybot.roleplay.abilities.trait.Trait

// an ability has one or more active/passive traits
data class Ability(val intId: Int, val stringId: String, val name: String,
                   val active: List<Trait>, val passive: List<Trait>) {

}

