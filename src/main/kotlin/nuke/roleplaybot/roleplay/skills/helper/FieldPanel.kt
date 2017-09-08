package nuke.roleplaybot.roleplay.skills.helper

import nuke.roleplaybot.roleplay.abilities.Element
import nuke.roleplaybot.roleplay.skills.SkillScope
import nuke.roleplaybot.roleplay.skills.SkillType
import nuke.roleplaybot.roleplay.skills.xml.Skill
import nuke.roleplaybot.roleplay.skills.xml.SkillStore
import nuke.roleplaybot.util.Wrapper
import nuke.roleplaybot.util.swing.CustomListCellRenderer
import nuke.roleplaybot.util.swing.addWithLabel
import nuke.roleplaybot.util.swing.makeCompactGrid
import nuke.roleplaybot.util.with
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.BorderFactory
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.JTextPane
import javax.swing.SpinnerNumberModel
import javax.swing.SpringLayout
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * Created by NuclearCoder on 2017-08-27.
 */

class FieldPanel(private val wizard: SkillCreationWizard, private val store: Wrapper<SkillStore?>) : JPanel(), DocumentListener {

    private var id = -1
    private val skill get(): Skill? = store.with { it.skills?.getOrNull(id) }

    private val name = JTextField().apply {
        document.addDocumentListener(this@FieldPanel)
    }
    private val message = JTextField()
    private val cost = JSpinner(SpinnerNumberModel(50, 0, Int.MAX_VALUE, 10))
    private val scope = JComboBox<SkillScope>().apply {
        renderer = CustomListCellRenderer(SkillScope::label)
        SkillScope.values().forEach(this::addItem)
    }

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

    private val description = JTextPane()

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

        border = BorderFactory.createEmptyBorder(4, 2, 4, 4)
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

        addFocusListener(object : FocusListener {
            override fun focusLost(e: FocusEvent) = save()
            override fun focusGained(e: FocusEvent) = load()
        })

        select(-1)
    }

    fun select(id: Int) {
        save()

        this.id = id
        load()
    }

    fun save() {
        // save the fields into the skill
        skill?.let {
            it.name = name.text
            it.cost = cost.value as Int
            it.scope = scope.selectedItem as SkillScope
            it.type = type.selectedItem as SkillType
            it.element = element.selectedItem as Element
            it.formula = formula.text
            it.variance = variance.value as Int
            //TODO: effects here
            it.message = message.text
            it.description = description.text
        }
    }

    fun load() {
        // load the fields from the skill
        skill?.let {
            name.text = it.name
            cost.value = it.cost
            scope.selectedItem = it.scope
            type.selectedItem = it.type
            element.selectedItem = it.element
            formula.text = it.formula
            variance.value = it.variance
            //TODO: effects here
            message.text = it.message
            description.text = it.description
        }
    }

    fun toggleFields(enable: Boolean) {
        name.isEnabled = enable
        cost.isEnabled = enable
        scope.isEnabled = enable
        type.isEnabled = enable
        element.isEnabled = enable
        formula.isEnabled = enable
        variance.isEnabled = enable
        //TODO: effects here
        message.isEnabled = enable
        description.isEnabled = enable
    }

    private fun updateName() {
        skill?.name = name.text
        wizard.repaint()
    }

    override fun changedUpdate(e: DocumentEvent) = updateName()
    override fun insertUpdate(e: DocumentEvent) = updateName()
    override fun removeUpdate(e: DocumentEvent) = updateName()

}