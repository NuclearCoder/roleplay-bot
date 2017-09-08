package nuke.roleplaybot.util.swing

import java.awt.event.ActionEvent
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem

/**
 * Created by NuclearCoder on 2017-08-27.
 */

inline fun JMenuBar(crossinline builder: JMenuBar.() -> Unit) = JMenuBar().apply(builder)

inline fun JMenuBar.menu(name: String, crossinline builder: JMenu.() -> Unit): JMenu = add(JMenu(name).apply(builder))
inline fun JMenu.menu(name: String, crossinline builder: JMenu.() -> Unit): JMenuItem = add(JMenu(name).apply(builder))
inline fun JMenu.item(name: String, crossinline action: (ActionEvent) -> Unit): JMenuItem = add(JMenuItem(name, action))
