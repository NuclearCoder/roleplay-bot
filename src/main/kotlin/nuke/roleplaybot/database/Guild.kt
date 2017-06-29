package nuke.roleplaybot.database

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Guilds : LongIdTable(name = "guilds") {
    val roleplayRoleId = long("rp_role").default(0)
}

class Guild(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Guild>(Guilds) {

        fun equals(guildId: Long) = (Guilds.id eq guildId)

        fun exists(guildId: Long) = transaction { Guilds.select(Guild.equals(guildId)).count() > 0 }

        fun getOrInit(guildId: Long) = transaction {
            Guild.find { Guild.equals(guildId) }.let {
                if (!it.empty()) {
                    it.first()
                } else {
                    Guild.new(guildId) {
                        this.roleplayRoleId = 0
                    }
                }
            }
        }
    }

    var roleplayRoleId by Guilds.roleplayRoleId

    operator fun compareTo(other: Guild) = id.value.compareTo(other.id.value)
}