package org.kud.roleplay.roleplay.message

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.roleplay.experience.ExperienceCalculator
import org.kud.roleplay.roleplay.user.User
import org.kud.roleplay.roleplay.user.experience.UserExperience

class MessageHandler {

    private val users = mutableMapOf<Pair<Long, Long>, User>()

    private val expCalculator = ExperienceCalculator()

    fun processMessage(event: MessageReceivedEvent, bot: RoleplayBot) {
        val message = event.message

        val guildId = message.guild.idLong
        val userId = message.author.idLong

        if (!bot.database.existsUser(guildId, userId)) {
            bot.database.initUser(guildId, userId)
        }

        val user = users.computeIfAbsent(Pair(guildId, userId)) {
            User(guildId, userId, bot.database.getUserExperience(guildId, userId) ?: UserExperience(0, 1.0))
        }

        val rawGain = expCalculator.calculate(message.content)
        val actualGain = (user.experience.multiplier * rawGain).toLong()

        bot.database.incrementUserExperience(guildId, userId, actualGain)
        user.experience.increment(actualGain)
    }

}