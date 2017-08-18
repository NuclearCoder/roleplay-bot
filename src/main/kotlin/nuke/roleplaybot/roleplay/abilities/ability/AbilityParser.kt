package nuke.roleplaybot.roleplay.abilities.ability

import nuke.roleplaybot.roleplay.abilities.trait.Trait
import nuke.roleplaybot.roleplay.abilities.trait.TraitContext
import nuke.roleplaybot.roleplay.abilities.trait.TraitParser
import nuke.roleplaybot.util.toIterable
import org.w3c.dom.Node
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory


object AbilityParser {

    private val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

    fun parse(inputStream: InputStream): List<Ability> {
        val document = documentBuilder.parse(inputStream)
        val root = document.documentElement
        if (root.tagName != "abilities") error("Required root \"abilities\" tag")
        root.normalize()

        return root.getElementsByTagName("ability").toIterable().mapIndexed(this::parseAbility)
    }

    // inefficient lookup but it won't be called much anyway.
    private fun parseAbility(id: Int, root: Node) = AbilityBuilder(id + 1).run {
        stringId = root.attributes.getNamedItem("id").textContent ?: error("Required \"id\" on ability node")

        var hasName = false
        var hasActive = false
        var hasPassive = false

        root.childNodes.toIterable().forEach {
            if (it.nodeType == Node.ELEMENT_NODE) when (it.nodeName) {
                "name" -> if (hasName) error("Duplicate \"name\" property for ability \"$stringId\"") else {
                    name = it.textContent
                    hasName = true
                }
                "active" -> if (hasActive) error("Duplicate \"active\" property for ability \"$stringId\"") else {
                    active { parseTraits(it, isActive = true) }
                    hasActive = true
                }
                "passive" -> if (hasPassive) error("Duplicate \"passive\" property for ability \"$stringId\"") else {
                    passive { parseTraits(it, isActive = false) }
                    hasPassive = true
                }
                else -> error("Illegal \"${it.nodeName}\" property for ability \"$stringId\"")
            }
        }

        build()
    }

    private fun parseTraits(root: Node, isActive: Boolean) = mutableListOf<Trait>().apply {
        root.childNodes.toIterable().forEach {
            if (it.nodeType == Node.ELEMENT_NODE) {
                add(TraitParser.parse(TraitContext(it, isActive = isActive)))
            }
        }
    }

}