package nuke.roleplaybot.roleplay.skills.xml

import org.jonnyzzz.kotlin.xml.bind.XProperty
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by NuclearCoder on 2017-09-04.
 */

val xmlDocumentBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()

abstract class PropertyDelegate<T>(private val prop: ReadWriteProperty<Any, String?>) : ReadWriteProperty<Any, T?> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T? = prop.getValue(thisRef, property)?.let(this::getValue)
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) = prop.setValue(thisRef, property, value.toString())

    abstract fun getValue(value: String): T
}

inline fun <T> propertyDelegate(prop: ReadWriteProperty<Any, String?>, crossinline getter: (String) -> T) = object : PropertyDelegate<T>(prop) {
    override fun getValue(value: String) = getter(value)
}

inline fun <reified T : Enum<T>> enumOf(prop: XProperty<String>) = propertyDelegate(prop) {
    try {
        enumValueOf<T>(it)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun intOf(prop: XProperty<String>) = propertyDelegate(prop, String::toIntOrNull)