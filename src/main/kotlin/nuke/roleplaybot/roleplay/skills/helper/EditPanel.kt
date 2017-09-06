package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.util.Wrapper
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.event.DocumentListener

/**
 * Created by NuclearCoder on 2017-08-27.
 */

class EditPanel(store: Wrapper<SkillStore?>) : JPanel() {

    lateinit var nameListener: DocumentListener

    private val panel = FieldPanel(store)

    init {
        border = BorderFactory.createEmptyBorder(4, 2, 4, 4)
        layout = BorderLayout()

        add(panel)
    }

    fun select(index: Int) = panel.select(index)
}