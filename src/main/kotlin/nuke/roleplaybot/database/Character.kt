package nuke.roleplaybot.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Characters : IntIdTable(name = "characters") {
    val guildId = long("id_guild")
    val userId = long("id_user")

    val name = varchar("name", 30)
    val content = text("content").default("")

    // stats

    val health = integer("stat_health").default(5)
    val mana = integer("stat_mana").default(7)
    val stamina = integer("stat_stamina").default(7)
    val strength = integer("stat_strength").default(1)
    val magic = integer("stat_magic").default(1)
    val defense = integer("stat_defense").default(0)
    val resistance = integer("stat_resistance").default(0)
    val speed = integer("stat_speed").default(0)
    val accuracy = integer("stat_accuracy").default(0)
    val skill = integer("stat_skill").default(0)

    // item slots

    val weapon1 = reference("weapon1", Items).nullable()
    val weapon2 = reference("weapon2", Items).nullable()
    val weapon3 = reference("weapon3", Items).nullable()
    val weapon4 = reference("weapon4", Items).nullable()
    val shield = reference("shield", Items).nullable()
    val helmet = reference("helmet", Items).nullable()
    val chestplate = reference("chestplate", Items).nullable()
    val leggings = reference("leggings", Items).nullable()
    val boots = reference("boots", Items).nullable()
    val amulet = reference("amulet", Items).nullable()
    val ring = reference("ring", Items).nullable()
    val consumable1 = reference("consumable1", Items).nullable()
    val consumable2 = reference("consumable2", Items).nullable()
}

class Character(id: EntityID<Int>) : IntEntity(id), Comparable<Character> {
    companion object : IntEntityClass<Character>(Characters) {
        fun equals(guildId: Long, userId: Long, name: String) =
                (Characters.guildId eq guildId) and
                        (Characters.userId eq userId) and
                        (Characters.name eq name)

        fun eqOp(guildId: Long, userId: Long, name: String): SqlExpressionBuilder.() -> Op<Boolean> =
                { equals(guildId, userId, name) }

        fun exists(guildId: Long, userId: Long, name: String) = transaction { Characters.select(equals(guildId, userId, name)).count() > 0 }
    }

    override fun compareTo(other: Character) =
            guildId.compareTo(other.guildId).let {
                if (it != 0) it
                else userId.compareTo(other.userId).let {
                    if (it != 0) it
                    else name.compareTo(other.name)
                }
            }

    override fun equals(other: Any?) = when (other) {
        is Character -> guildId == other.guildId && userId == other.userId && name == other.name
        else -> super.equals(other)
    }

    override fun hashCode() = 31 * (31 * (31 * name.hashCode() + userId.hashCode()) + guildId.hashCode())

    var guildId by Characters.guildId
    var userId by Characters.userId

    var name by Characters.name
    var content by Characters.content

    var statHealth by Characters.health
    var statMana by Characters.mana
    var statStamina by Characters.stamina
    var statStrength by Characters.strength
    var statMagic by Characters.magic
    var statDefense by Characters.defense
    var statResistance by Characters.resistance
    var statSpeed by Characters.speed
    var statAccuracy by Characters.accuracy
    var statSkill by Characters.skill

    var weapon1 by Item optionalReferencedOn Characters.weapon1
    var weapon2 by Item optionalReferencedOn Characters.weapon2
    var weapon3 by Item optionalReferencedOn Characters.weapon3
    var weapon4 by Item optionalReferencedOn Characters.weapon4
    var shield by Item optionalReferencedOn Characters.shield
    var helmet by Item optionalReferencedOn Characters.helmet
    var chestplate by Item optionalReferencedOn Characters.chestplate
    var leggings by Item optionalReferencedOn Characters.leggings
    var boots by Item optionalReferencedOn Characters.boots
    var amulet by Item optionalReferencedOn Characters.amulet
    var ring by Item optionalReferencedOn Characters.ring
    var consumable1 by Item optionalReferencedOn Characters.consumable1
    var consumable2 by Item optionalReferencedOn Characters.consumable2

}