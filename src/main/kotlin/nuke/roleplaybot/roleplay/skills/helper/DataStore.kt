package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.skills.xml.SkillStore
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

class DataStore(val fields: EditPanel, val list: ListPanel) {

    private inline val parent get() = list.parent

    var file: File? = null

    var lastSaved = true

    fun new() {
        when (areYouSure()) {
            JOptionPane.YES_OPTION -> save()
            JOptionPane.NO_OPTION -> {
            }
            JOptionPane.CANCEL_OPTION -> return
        }

        clearFields()
        file = null
        list.create()
    }

    fun save(saveAs: Boolean = false) {
        if (file != null && saveAs && chooseFile(save = true)) {
            saveSkills()
            lastSaved = true
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

    private fun areYouSure() = JOptionPane.showConfirmDialog(parent, "Do you want to save changes?", "Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)

    private fun clearFields() {
        fields.removeAll()
        fields.set.clear()
        list.elements.clear()
    }

    /* save:   true  => save file dialog
               false => load file dialog

       return: true  => a file was chosen
               false => no file was chosen (error or cancel)
     */
    private fun chooseFile(save: Boolean = false): Boolean {
        val chooser = JFileChooser(file?.parent)
        val choice = if (save) chooser.showSaveDialog(parent) else chooser.showSaveDialog(parent)
        if (choice == JFileChooser.APPROVE_OPTION) {
            file = chooser.selectedFile
            return true
        }
        return false
    }

    private fun saveSkills() {
        //TODO
        val skill: SkillStore
        val element = JDOM.save(skill, SkillStore::class.java)

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
            //TODO
        }
    }

}