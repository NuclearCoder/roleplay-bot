package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.util.swing.JMenuBar
import nuke.roleplaybot.util.swing.item
import nuke.roleplaybot.util.swing.menu
import javax.swing.JFrame
import javax.swing.JSplitPane

class SkillCreationWizard : JSplitPane(JSplitPane.HORIZONTAL_SPLIT) {

    private val container = JFrame("Skill Creation Wizard").apply {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        contentPane = this@SkillCreationWizard
    }

    private val fields = EditPanel()
    private val list = ListPanel(fields)

    private val data = DataStore(fields, list)

    private val menuBar = JMenuBar {
        menu("File") {
            item("New") { data.new() }
            item("Open") { data.open() }
            item("Save") { data.save() }
            item("Save As...") { data.save(saveAs = true) }
        }
    }

    init {
        add(fields, JSplitPane.RIGHT)
        add(list, JSplitPane.LEFT)

        container.jMenuBar = menuBar

        container.pack()
        container.minimumSize = container.size
        container.isVisible = true
    }

}