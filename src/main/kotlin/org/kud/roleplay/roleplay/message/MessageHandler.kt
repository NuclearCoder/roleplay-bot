package org.kud.roleplay.roleplay.message

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.roleplay.experience.ExperienceCalculator

class MessageHandler {

    private val expCalculator = ExperienceCalculator()

    fun processMessage(event: MessageReceivedEvent, bot: RoleplayBot) {
        if (event.author.isBot) return

        val message = event.message

        val guildId = message.guild.idLong
        val userId = message.author.idLong

        if (!bot.database.existsUser(guildId, userId)) {
            bot.database.initUser(guildId, userId)
        }

        val user = bot.users[guildId, userId]

        val rawGain = expCalculator.calculate(message.content)
        val actualGain = (user.experience.multiplier * rawGain).toLong()

        bot.database.incrementUserExperience(guildId, userId, actualGain)
        user.experience.increment(actualGain)
    }

}