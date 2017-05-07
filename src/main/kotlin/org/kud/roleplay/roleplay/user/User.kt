package org.kud.roleplay.roleplay.user

import org.kud.roleplay.roleplay.user.experience.UserExperience

data class User(val guildId: Long, val id: Long, val experience: UserExperience)