package org.kud.roleplay.roleplay.server

import org.kud.roleplay.database.Database
import org.kud.roleplay.roleplay.experience.UserExperience

class Users(private val database: Database) {

    private val guilds = mutableMapOf<Long, MutableMap<Long, User>>()

    operator fun get(guildId: Long) = guilds.computeIfAbsent(guildId) { mutableMapOf() }

    operator fun get(guildId: Long, userId: Long) = get(guildId).computeIfAbsent(userId) {
        User(guildId, userId, database.getUserExperience(guildId, userId) ?: UserExperience(0, 1.0))
    }

}