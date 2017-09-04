package nuke.roleplaybot.roleplay.skills.helper

import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.ListCellRenderer

/**
 * Created by NuclearCoder on 2017-08-25.
 */

class CustomListCellRenderer<T>(private val transform: (T) -> String) : ListCellRenderer<T> {
    private val renderer = DefaultListCellRenderer()

    override fun getListCellRendererComponent(list: JList<out T>?, value: T, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        return renderer.getListCellRendererComponent(list, transform(value), index, isSelected, cellHasFocus)
    }
}