package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.set
import nuke.roleplaybot.util.with
import org.jdom2.input.SAXBuilder
import org.jdom2.output.Format
import org.jdom2.output.XMLOutputter
import org.jonnyzzz.kotlin.xml.bind.jdom.JDOM
import java.io.File
import javax.swing.JFileChooser
import javax.swing.JOptionPane


/**
 * Created by NuclearCoder on 2017-09-02.
 */

class DataStore(private val wizard: SkillCreationWizard, private val store: Wrapper<SkillStore?>) {//, private val fields: FieldPanel, private val list: ListPanel) {

    var file: File? = null

    var changed = true

    fun new() {
        when (areYouSure()) {
            JOptionPane.YES_OPTION -> save()
            JOptionPane.NO_OPTION -> {
            }
            JOptionPane.CANCEL_OPTION -> return
        }

        wizard.toggleFields(false)
        file = null
        wizard.createListEntry()
    }

    fun save(saveAs: Boolean = false) {
        if ((saveAs || file == null) && chooseFile(save = true) && file != null) {
            saveSkills()
            changed = false
        }
    }

    fun open() {
        when (areYouSure()) {
            JOptionPane.YES_OPTION -> save()
            JOptionPane.NO_OPTION -> {
            }
            JOptionPane.CANCEL_OPTION -> return
        }

        chooseFile()
        loadSkills()
    }

    private fun areYouSure() = JOptionPane.showConfirmDialog(wizard, "Do you want to save changes?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)

    /* save:   true  => save file dialog
               false => load file dialog

       return: true  => a file was chosen
               false => no file was chosen (error or cancel)
     */
    private fun chooseFile(save: Boolean = false): Boolean {
        val chooser = JFileChooser(file?.parent)
        val choice = if (save) chooser.showSaveDialog(wizard) else chooser.showSaveDialog(wizard)
        if (choice == JFileChooser.APPROVE_OPTION) {
            file = chooser.selectedFile
            return true
        }
        return false
    }

    private fun saveSkills() {
        val element = store.with { JDOM.save(it, SkillStore::class.java) }

        file?.bufferedWriter().use {
            XMLOutputter().apply {
                format = Format.getPrettyFormat()
                output(element, it)
            }
        }
    }

    private fun loadSkills() {
        file?.bufferedReader().use {
            val element = SAXBuilder().build(it).rootElement
            val skill = JDOM.load(element, SkillStore::class.java)

            store.set(skill)
        }
    }

}