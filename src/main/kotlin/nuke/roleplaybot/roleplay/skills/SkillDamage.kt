package nuke.roleplaybot.roleplay.skills

import nuke.roleplaybot.roleplay.abilities.Element

data class SkillDamage(
        var type: SkillType = SkillType.HP_DAMAGE,
        var element: Element = Element.FIRE,
        var formula: String = "", // math formula TODO: appropriate type
        var variance: Int = 20 // by how much output can vary on both ends (bounded by zero)
)