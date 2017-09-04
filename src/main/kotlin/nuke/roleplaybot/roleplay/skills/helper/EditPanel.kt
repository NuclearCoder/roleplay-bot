package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.SkillDescriptor
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.event.DocumentListener

/**
 * Created by NuclearCoder on 2017-08-27.
 */

class EditPanel : JPanel() {

    lateinit var nameListener: DocumentListener

    val set: MutableMap<Int, FieldPanel> = mutableMapOf()


    init {
        border = BorderFactory.createEmptyBorder(4, 2, 4, 4)
        layout = BorderLayout()

        FieldPanel(SkillDescriptor(0)).let {
            add(it)
            set[0] = it
        }
    }

    fun select(index: Int) {
        if (componentCount < 1) return

        getComponent(0).let { comp ->
            (comp as FieldPanel).name.document.removeDocumentListener(nameListener)
            remove(comp)
        }

        set[index]?.let { comp ->
            add(comp)
            comp.name.document.addDocumentListener(nameListener)
        }
        revalidate()
        repaint()
    }

}