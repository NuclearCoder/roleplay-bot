package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.with
import javax.swing.AbstractListModel

/**
 * Created by NuclearCoder on 2017-09-06.
 */
class WrapperListModel(val list: Wrapper<SkillStore?>) : AbstractListModel<String>() {

    override fun getSize(): Int = list.with { it.skills?.size } ?: 0

    override fun getElementAt(index: Int): String = list.with {
        it.skills?.get(index)?.name?.let { name -> "$index: $name" }
    } ?: error("List wrapper not set")

}