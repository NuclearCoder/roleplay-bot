package nuke.roleplaybot.roleplay.abilities.ability

import nuke.roleplaybot.roleplay.abilities.trait.Trait

class AbilityBuilder(private val id: Int) {

    lateinit var stringId: String
    lateinit var name: String

    private var active = emptyList<Trait>()
    fun active(build: () -> List<Trait>) {
        active = build()
    }

    private var passive = emptyList<Trait>()
    fun passive(build: () -> List<Trait>) {
        passive = build()
    }

    fun build() = Ability(
            intId = id, stringId = stringId, name = name,
            active = active, passive = passive
    )

}