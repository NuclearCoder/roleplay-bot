package org.kud.roleplay.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.insertIgnore

object Items : IntIdTable(name = "items") {
    enum class ItemType {
        PHYSICAL_WEAPON, SHIELD, MAGICAL_WEAPON,
        HELMET, CHESTPLATE, LEGGINGS, BOOTS,
        AMULET, RING, CONSUMABLE
    }

    val type = enumeration("type", ItemType::class.java).default(ItemType.CONSUMABLE) // indexed from 0, corresponds to each ItemType
    val tier = integer("tier").default(0)
    val name = varchar("name", 50).default("")

    val price = integer("price").default(0)

    // stats

    val health = integer("health").default(0)
    val mana = integer("mana").default(0)
    val stamina = integer("stamina").default(0)
    val strength = integer("strength").default(0)
    val magic = integer("magic").default(0)
    val defense = integer("defense").default(0)
    val resistance = integer("resistance").default(0)
    val speed = integer("speed").default(0)
    val accuracy = integer("accuracy").default(0)
    val skill = integer("skill").default(0)

}

class Item(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Item>(Items) {
        val nothing = EntityID(-1, Items)

        init {
            Items.insertIgnore { it[Items.id] = nothing }
        }
    }

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

