package org.kud.roleplay.roleplay.server

import org.kud.roleplay.roleplay.experience.UserExperience

data class User(val guildId: Long, val id: Long, val experience: UserExperience)