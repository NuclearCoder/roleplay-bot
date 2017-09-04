package nuke.roleplaybot.roleplay.skills.xml

import nuke.roleplaybot.roleplay.abilities.Element
import nuke.roleplaybot.roleplay.skills.SkillScope
import nuke.roleplaybot.roleplay.skills.SkillType
import org.jonnyzzz.kotlin.xml.bind.XElements
import org.jonnyzzz.kotlin.xml.bind.XSub
import org.jonnyzzz.kotlin.xml.bind.XText
import org.jonnyzzz.kotlin.xml.bind.jdom.JXML

/**
 * Created by NuclearCoder on 2017-09-03.
 */

class Skill {
    var name by JXML / "general" / "name" / XText - ""
    var message by JXML / "general" / "message" / XText - ""
    var scope by enumOf<SkillScope>(JXML / "general" / "scope" / XText - SkillScope.MELEE.toString())
    var cost by intOf(JXML / "general" / "cost" / XText - "20")

    var type by enumOf<SkillType>(JXML / "damage" / "type" / XText - SkillType.HP_DAMAGE.toString())
    var element by enumOf<Element>(JXML / "damage" / "element" / XText - Element.FIRE.toString())
    var formula by JXML / "damage" / "type" / XText - ""
    var variance by intOf(JXML / "damage" / "variance" / XText - "10")

    var effects by JXML / "effects" / XElements("effect") / XSub(SkillEffect::class.java)
}
