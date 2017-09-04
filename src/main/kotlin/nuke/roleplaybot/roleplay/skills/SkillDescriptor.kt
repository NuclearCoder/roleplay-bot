package nuke.roleplaybot.roleplay.skills

import nuke.roleplaybot.roleplay.skills.xml.SkillEffect

data class SkillDescriptor(
        var id: Int, var name: String = "",
        var description: String = "",
        var cost: Int = 20, // mana
        var scope: SkillScope = SkillScope.MELEE,
        val damage: SkillDamage = SkillDamage(), // damage or heal
        val effects: List<SkillEffect> = mutableListOf(),
        var message: String = ""
)


