package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.abilities.Element
import nuke.roleplaybot.roleplay.skills.SkillDescriptor
import nuke.roleplaybot.roleplay.skills.SkillScope
import nuke.roleplaybot.roleplay.skills.SkillType
import nuke.roleplaybot.util.swing.addWithLabel
import nuke.roleplaybot.util.swing.makeCompactGrid
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.SpinnerNumberModel
import javax.swing.SpringLayout

/**
 * Created by NuclearCoder on 2017-08-25.
 */

class FieldPanel(val skill: SkillDescriptor) : JPanel() {

    val name = JTextField()
    private val message = JTextField()

    private val cost = JSpinner(SpinnerNumberModel(50, 0, Int.MAX_VALUE, 10))

    private val scope = JComboBox<SkillScope>().apply {
        renderer = CustomListCellRenderer(SkillScope::label)
        SkillScope.values().forEach(this::addItem)
    }

    private val radius = JSpinner(SpinnerNumberModel(1, 0, Int.MAX_VALUE, 1))

    private val description = JTextPane()

    private val type = JComboBox<SkillType>().apply {
        renderer = CustomListCellRenderer(SkillType::label)
        SkillType.values().forEach(this::addItem)
    }

    private val element = JComboBox<Element>().apply {
        renderer = CustomListCellRenderer(Element::label)
        Element.values().forEach(this::addItem)
    }

    private val formula = JTextField()
    private val variance = JSpinner(SpinnerNumberModel(20, 0, Int.MAX_VALUE, 10))

    private val effects = JList<String>()

    init {
        /*
         - General            |   - Effects
        [Name]                |  [Effects]
        [Description]         |
        [Scope]   [Cost]      |
         - Damage             |
        [Type]    [Element]   |
        [Formula] [Variance]  |
         - Message            |
        [Message]             |
         */

        layout = GridLayout(0, 2)

        val left = JPanel(GridLayout(0, 1, 0, 8)).apply {
            val general = JPanel(SpringLayout()).apply {
                border = BorderFactory.createTitledBorder("General")
                addWithLabel("Name: ", this@FieldPanel.name)
                addWithLabel("Message: ", this@FieldPanel.message)
                addWithLabel("Scope: ", this@FieldPanel.scope)
                addWithLabel("Cost: ", this@FieldPanel.cost)
                makeCompactGrid(4, 2)
            }
            val damage = JPanel(SpringLayout()).apply {
                border = BorderFactory.createTitledBorder("Damage")
                addWithLabel("Type: ", this@FieldPanel.type)
                addWithLabel("Element: ", this@FieldPanel.element)
                addWithLabel("Formula: ", this@FieldPanel.formula)
                addWithLabel("Variance: ", this@FieldPanel.variance)
                makeCompactGrid(4, 2)
            }
            val message = JPanel(BorderLayout()).apply {
                border = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Description"), BorderFactory.createEmptyBorder(4, 4, 4, 4))
                add(this@FieldPanel.description, BorderLayout.CENTER)
            }

            add(general)
            add(damage)
            add(message)
        }
        val right = JPanel(BorderLayout()).apply {
            border = BorderFactory.createTitledBorder("Effects")

            add(this@FieldPanel.effects, BorderLayout.EAST)
        }

        add(left)
        add(right)
    }

    fun update() {
        skill.name = name.text
        skill.description = description.text
        skill.cost = cost.value as Int
        skill.scope = scope.selectedItem as SkillScope
        skill.damage.type = type.selectedItem as SkillType
        skill.damage.element = element.selectedItem as Element
        skill.damage.formula = formula.text
        skill.damage.variance = variance.value as Int
        //TODO: effects here
        skill.message = message.text
    }

}