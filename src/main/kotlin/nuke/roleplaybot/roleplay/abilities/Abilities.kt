package nuke.roleplaybot.roleplay.abilities

import nuke.roleplaybot.roleplay.abilities.ability.Ability
import nuke.roleplaybot.roleplay.abilities.ability.AbilityParser

object Abilities {

    val values = javaClass.getResourceAsStream("/abilities.xml").buffered().use(AbilityParser::parse)
    val byStringId = values.associateBy(Ability::stringId)

    operator fun get(intId: Int) = values[intId - 1] // remember that integer IDs begin from ONE
    operator fun get(stringId: String) = byStringId[stringId]

}