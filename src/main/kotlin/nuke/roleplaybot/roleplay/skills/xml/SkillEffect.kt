package nuke.roleplaybot.roleplay.skills.xml

import nuke.roleplaybot.roleplay.abilities.StatusType
import org.jonnyzzz.kotlin.xml.bind.XAttribute
import org.jonnyzzz.kotlin.xml.bind.jdom.JXML

class SkillEffect {
    var type by JXML / XAttribute("type") - "add"
    var status by enumOf<StatusType>(JXML / XAttribute("status") - StatusType.STUN.toString())
}