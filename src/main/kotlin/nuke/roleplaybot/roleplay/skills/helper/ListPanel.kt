package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.roleplay.skills.xml.create
import nuke.roleplaybot.roleplay.skills.xml.removeAt
import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.swing.JButton
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent

/**
 * Created by NuclearCoder on 2017-08-25.
 */

class ListPanel(private val wizard: SkillCreationWizard, private val store: Wrapper<SkillStore?>) : JPanel() {

    private val model = WrapperListModel(store)
    private val listSkills = JList(model).apply {
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

        create()
    }

    fun create() {
        val last = model.size

        store.create()

        if (model.size > 0) wizard.toggleFields(true)

        wizard.selectSkill(last)
        listSkills.selectedIndex = last

        wizard.repaint()
    }

    fun remove() {
        val cur = listSkills.selectedIndex
        if (cur < 0 || cur >= model.size) return

        store.removeAt(cur)

        if (model.size == 0) wizard.toggleFields(false)

        wizard.selectSkill(cur - 1)
        listSkills.selectedIndex = cur - 1

        wizard.repaint()
    }

    private fun updateSelected(e: ListSelectionEvent) {
        if (!e.valueIsAdjusting) {
            wizard.selectSkill(listSkills.selectedIndex)
        }
    }

}