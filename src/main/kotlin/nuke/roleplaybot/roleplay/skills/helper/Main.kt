package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.util.swing.trySetLNF
import javax.swing.SwingUtilities

/**
 * Created by NuclearCoder on 2017-08-25.
 */

fun main(args: Array<String>) = SwingUtilities.invokeLater {
    trySetLNF()
    SkillCreationWizard()
}