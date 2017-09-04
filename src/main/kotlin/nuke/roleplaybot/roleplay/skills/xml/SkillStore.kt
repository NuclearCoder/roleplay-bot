package nuke.roleplaybot.roleplay.skills.xml

import org.jonnyzzz.kotlin.xml.bind.XElements
import org.jonnyzzz.kotlin.xml.bind.XSub
import org.jonnyzzz.kotlin.xml.bind.jdom.JXML

/**
 * Created by NuclearCoder on 2017-09-05.
 */

class SkillStore {
    var skills by JXML / "skills" / XElements("skill") / XSub(Skill::class.java)
}