package nuke.roleplaybot.util.swing

import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JMenu
import javax.swing.JMenuItem

/**
 * Created by NuclearCoder on 2017-08-27.
 */

inline fun AbstractAction(name: String, crossinline action: (ActionEvent) -> Unit) = object : AbstractAction(name) {
    override fun actionPerformed(e: ActionEvent) = action(e)
}

inline fun JButton(label: String, crossinline action: (ActionEvent) -> Unit): JButton = JButton(AbstractAction(label, action))
inline fun JMenuItem(label: String, crossinline action: (ActionEvent) -> Unit): JMenuItem = JMenuItem(AbstractAction(label, action))
inline fun JMenu(label: String, crossinline action: (ActionEvent) -> Unit): JMenu = JMenu(AbstractAction(label, action))