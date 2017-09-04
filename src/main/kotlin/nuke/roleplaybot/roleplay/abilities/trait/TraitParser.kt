package nuke.roleplaybot.roleplay.abilities.trait

import nuke.roleplaybot.database.Items.ItemType
import nuke.roleplaybot.roleplay.abilities.StatusType

object TraitParser {

    private infix fun String.to(builder: TraitContext.() -> Trait) = Pair(this, builder)
    private val builders = mapOf(
            "slot" to {
                requirePassive()
                node.getEnum<ItemType>("type").let(Trait::Slot)
            },
            "throw" to {
                requireActive()
                node.getInt("range").let(Trait::Throw)
            },
            "throwre" to {
                requireActive()
                node.getInt("range").let(Trait::Throwre)
            },
            "status" to {
                val status = node.getEnum<StatusType>("type")
                val chance = node.getInt("chance")
                Trait.Status(status, chance, isActive)
            }
    )

    fun parse(context: TraitContext): Trait = context.node.nodeName.let {
        builders[it]?.invoke(context) ?: error("Illegal trait type \"$it\"")
    }

}