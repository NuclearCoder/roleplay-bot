package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.swing.JButton
import nuke.roleplaybot.util.swing.text
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.DefaultListModel
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListSelectionModel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.event.ListSelectionEvent

/**
 * Created by NuclearCoder on 2017-08-25.
 */

class ListPanel(val store: Wrapper<SkillStore?>, private val fields: EditPanel) : JPanel(), DocumentListener {

    val elements = DefaultListModel<String>()
    private val listSkills = JList<String>(elements).apply {
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        addListSelectionListener(this@ListPanel::updateSelected)
    }

    private val buttonCreate = JButton("+") { create() }
    private val buttonRemove = JButton("-") { remove() }

    init {
        border = BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 2), BorderFactory.createTitledBorder("Skills"))
        layout = BorderLayout()

        add(JScrollPane(listSkills), BorderLayout.CENTER)
        add(JPanel(FlowLayout()).apply {
            add(buttonCreate)
            add(buttonRemove)
        }, BorderLayout.SOUTH)

        fields.nameListener = this

        create()
    }

    fun create() {
        val last = elements.size

        elements.addElement("$last:")
        fields.select(last)

        listSkills.selectedIndex = last

        if (elements.size > 1) buttonRemove.isEnabled = true
    }

    fun remove() {
        val cur = listSkills.selectedIndex
        if (cur < 0 || cur >= elements.size) return

        elements.remove(cur)
        fields.select(cur - 1)

        /*for (index in cur until elements.size) {
            val comp = store ?: continue
            comp.skill.id--
            fields.set[index - 1] = comp
        }*/

        if (elements.size <= 1) buttonRemove.isEnabled = false
    }

    private fun updateSelected(e: ListSelectionEvent) {
        if (!e.valueIsAdjusting) {
            fields.select(listSkills.selectedIndex)
        }
    }

    private fun updateName(text: String) {
        listSkills.selectedIndex.let {
            elements[it] = "$it: $text"
        }
    }

    override fun changedUpdate(e: DocumentEvent) = updateName(e.document.text)
    override fun insertUpdate(e: DocumentEvent) = updateName(e.document.text)
    override fun removeUpdate(e: DocumentEvent) = updateName(e.document.text)

}