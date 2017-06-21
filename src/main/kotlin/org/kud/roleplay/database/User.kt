package org.kud.roleplay.database

import net.dv8tion.jda.core.entities.Member
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
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.roleplay.experience.getLevelFor
import java.math.BigDecimal

/**
 * Created by NuclearCoder on 5/19/2017.
 */

object Users : IntIdTable(name = "users") {
    val guildId = long("id_guild")
    val userId = long("id_user")

    val activeCharacter = varchar("active_character", 30).nullable()

    val experienceTotal = long("exp_total").default(0)
    val experienceMultiplier = decimal("exp_mult", 20, 2).default(BigDecimal(1))
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users) {

        fun equals(guildId: Long, userId: Long) =
                (Users.guildId eq guildId) and (Users.userId eq userId)

        fun eqOp(guildId: Long, userId: Long): SqlExpressionBuilder.() -> Op<Boolean> =
                { User.equals(guildId, userId) }

        fun exists(guildId: Long, userId: Long) = transaction { Users.select(equals(guildId, userId)).count() > 0 }

        fun getOrInit(guildId: Long, userId: Long) = transaction {
            User.find { equals(guildId, userId) }.let {
                if (!it.empty()) {
                    it.first()
                } else {
                    User.new {
                        this.guildId = guildId
                        this.userId = userId
                        this.activeCharacter = null
                        this.experienceTotal = 0
                        this.experienceMultiplier = BigDecimal(1)
                    }
                }
            }
        }
    }

    var guildId by Users.guildId
    var userId by Users.userId

    var activeCharacter by Users.activeCharacter

    var experienceTotal by Users.experienceTotal
    var experienceMultiplier by Users.experienceMultiplier

    private val _levelAndExp get() = getLevelFor(experienceTotal)
    val experienceLevel get() = _levelAndExp.first
    val experienceRemaining get() = _levelAndExp.second

    operator fun compareTo(other: User) =
            guildId.compareTo(other.guildId).let {
                if (it != 0) it
                else userId.compareTo(other.userId)
            }
}

/**
 * Returns the active character for the given user and guild IDs (if any)
 */
fun Long.getActiveCharacter(guildId: Long): Character? {
    val userId = this
    return transaction {
        val user = User.getOrInit(guildId, userId)
        val character = user.activeCharacter
        character?.let { Character.find(Character.eqOp(guildId, userId, it)).firstOrNull() }
    }
}

fun Member.getActiveCharacter() = user.idLong.getActiveCharacter(guild.idLong)

fun CommandContext.getActiveCharacter() = event.member.getActiveCharacter()