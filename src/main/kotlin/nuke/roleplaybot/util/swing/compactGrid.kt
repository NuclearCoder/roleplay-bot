package nuke.roleplaybot.util.swing

import java.awt.Component
import java.awt.Container
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.Spring
import javax.swing.SpringLayout
import javax.swing.text.Document

/**
 * Created by NuclearCoder on 2017-08-27.
 */

val Document.text get(): String = getText(0, length)

fun JComponent.addWithLabel(label: String, comp: Component): Component = JLabel(label).let {
    it.labelFor = comp
    add(it)
    add(comp)
}

private fun Container.getConstraintsForCell(row: Int, col: Int, cols: Int) =
        (layout as SpringLayout).getConstraints(getComponent(row * cols + col))

fun Container.makeCompactGrid(rows: Int, cols: Int, initialX: Int = 6, initialY: Int = 6, xPad: Int = 6, yPad: Int = 6) {
    val layout = layout as? SpringLayout ?: error("The container must use layout SpringLayout.")

    //Align all cells in each column and make them the same width.
    val x = (0 until cols).fold(Spring.constant(initialX)) { x, c ->
        val width = (0 until rows).fold(Spring.constant(0)) { w, r ->
            Spring.max(w, getConstraintsForCell(r, c, cols).width)
        }
        (0 until rows).forEach { r ->
            getConstraintsForCell(r, c, cols).let {
                it.x = x
                it.width = width
            }
        }
        Spring.sum(x, Spring.sum(width, Spring.constant(xPad)))
    }

    //Align all cells in each row and make them the same height.
    val y = (0 until rows).fold(Spring.constant(initialY)) { y, r ->
        val height = (0 until cols).fold(Spring.constant(0)) { h, c ->
            Spring.max(h, getConstraintsForCell(r, c, cols).height)
        }
        (0 until cols).forEach { c ->
            getConstraintsForCell(r, c, cols).let {
                it.y = y
                it.height = height
            }
        }
        Spring.sum(y, Spring.sum(height, Spring.constant(yPad)))
    }

    //Set the parent's size.
    layout.getConstraints(this).apply {
        setConstraint(SpringLayout.SOUTH, y)
        setConstraint(SpringLayout.EAST, x)
    }
}