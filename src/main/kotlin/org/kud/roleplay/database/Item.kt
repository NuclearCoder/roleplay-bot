package org.kud.roleplay.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

/**
 * Created by NuclearCoder on 21/06/17.
 */

object Items : IntIdTable(name = "items") {
    enum class ItemType {
        PHYSICAL_WEAPON, SHIELD, MAGICAL_WEAPON,
        HELMET, CHESTPLATE, LEGGINGS, BOOTS,
        AMULET, RING, CONSUMABLE
    }

    val type = enumeration("type", ItemType::class.java) // indexed from 0, corresponds to each ItemType
    val tier = integer("tier")
    val name = varchar("name", 50)

    val price = integer("price")

    // stats

    val health = integer("health")
    val mana = integer("mana")
    val stamina = integer("stamina")
    val strength = integer("strength")
    val magic = integer("magic")
    val defense = integer("defense")
    val resistance = integer("resistance")
    val speed = integer("speed")
    val accuracy = integer("accuracy")
    val skill = integer("skill")

}

class Item(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Item>(Items)

    val type by Items.type
    val tier by Items.tier
    val name by Items.name

    val price by Items.price

    val health by Items.health
    val mana by Items.mana
    val stamina by Items.stamina
    val strength by Items.strength
    val magic by Items.magic
    val defense by Items.defense
    val resistance by Items.resistance
    val speed by Items.speed
    val accuracy by Items.accuracy
    val skill by Items.skill

}

