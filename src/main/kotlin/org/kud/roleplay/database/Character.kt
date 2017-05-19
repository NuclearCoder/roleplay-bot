package org.kud.roleplay.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

/**
 * Created by NuclearCoder on 5/18/2017.
 */

object Characters : IntIdTable(name = "characters") {
    val guildId = long("id_guild")
    val userId = long("id_user")

    val name = varchar("name", 50)
    val content = text("content").default("")
}

class Character(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Character>(Characters) {
        fun equals(guildId: Long, userId: Long, name: String) =
                (Characters.guildId eq guildId) and
                        (Characters.userId eq userId) and
                        (Characters.name eq name)

        fun eqOp(guildId: Long, userId: Long, name: String): SqlExpressionBuilder.() -> Op<Boolean> =
                { equals(guildId, userId, name) }

        fun exists(guildId: Long, userId: Long, name: String) =
                Characters.select(equals(guildId, userId, name)).count() > 0
    }

    var guildId by Characters.guildId
    var userId by Characters.userId

    var name by Characters.name
    var content by Characters.content

    infix operator fun compareTo(other: Character) =
            guildId.compareTo(other.guildId).let {
                if (it != 0) it
                else userId.compareTo(other.userId).let {
                    if (it != 0) it
                    else name.compareTo(other.name)
                }
            }
}