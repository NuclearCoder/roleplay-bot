package nuke.roleplaybot.roleplay.skills.xml

import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.set
import nuke.roleplaybot.util.with
import org.jdom2.Element
import org.jdom2.filter.ElementFilter
import org.jonnyzzz.kotlin.xml.bind.XElements
import org.jonnyzzz.kotlin.xml.bind.XRoot
import org.jonnyzzz.kotlin.xml.bind.XSub
import org.jonnyzzz.kotlin.xml.bind.jdom.JDOM
import org.jonnyzzz.kotlin.xml.bind.jdom.JXML

/**
 * Created by NuclearCoder on 2017-09-05.
 */

@XRoot(name = "skills")
class SkillStore {

    var skills by JXML / XElements("skill") / XSub(Skill::class.java)

}

fun Wrapper<SkillStore?>.create() {
    with { JDOM.save(it, SkillStore::class.java) }?.let { root ->
        val skill = Skill(id = root.getChildren("skill").size)
        val node = JDOM.save(skill, Skill::class.java)
        root.addContent(node)
        set(JDOM.load(root, SkillStore::class.java))
    } ?: error("List wrapper not set")
}

fun Wrapper<SkillStore?>.removeAt(id: Int) {
    with { JDOM.save(it, SkillStore::class.java) }?.let { root ->

        // remove skill node with that id
        root.removeContent(object : ElementFilter("skill") {
            override fun filter(content: Any?): Element? = super.filter(content)?.let {
                if (id.toString() == it.getAttributeValue("id")) it else null
            }
        })

        set(JDOM.load(root, SkillStore::class.java))
    } ?: error("List wrapper not set")
}

