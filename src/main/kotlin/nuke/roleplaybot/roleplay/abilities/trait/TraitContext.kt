package nuke.roleplaybot.roleplay.abilities.trait

import nuke.roleplaybot.util.notNull
import org.w3c.dom.Node

@Suppress("NOTHING_TO_INLINE")
class TraitContext(val node: Node, val isActive: Boolean) {

    fun require(message: String = "Condition is false.", condition: Boolean) {
        if (!condition) error(message)
    }
    inline fun require(message: String = "Predicate is false.", predicate: TraitContext.() -> Boolean) {
        require(predicate())
    }
    fun requireActive() = require("This trait can only be active.", isActive)
    fun requirePassive() = require("This trait can only be passive.", !isActive)

    fun Node.getNode(key: String): Node? = attributes.getNamedItem(key)
    fun Node.getOrNull(key: String): String? = attributes.getNamedItem(key)?.textContent
    fun Node.getString(key: String): String = getOrNull(key).notNull("Attribute \$key\" not found.")
    inline operator fun Node.get(key: String) = getString(key)

    fun Node.getInt(key: String): Int = try {
        getString(key).toInt()
    } catch (e: NumberFormatException) {
        error("Attribute \"$key\" must be an integer.")
    }

    // match attribute to enum field (ex: "A" -> Enum.A)
    inline fun <reified T : Enum<T>> Node.getEnum(key: String): T = try {
        enumValueOf(getString(key))
    } catch (e: IllegalArgumentException) {
        error("Attribute \"$key\" must be one of the following: ${enumValues<T>()}")
    }

}