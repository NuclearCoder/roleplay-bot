package nuke.roleplaybot.roleplay.message

import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import nuke.roleplaybot.database.User
import nuke.roleplaybot.database.Users
import nuke.roleplaybot.roleplay.experience.ExperienceCalculator
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object MessageHandler {

    fun processMessage(event: MessageReceivedEvent) {
        if (event.author.isBot) return

        val message = event.message

        val guildId = message.guild.idLong
        val userId = message.author.idLong

        val user = User.getOrInit(guildId, userId)

        val rawGain = ExperienceCalculator.calculate(message.content)
        val actualGain = (user.experienceMultiplier.toDouble() * rawGain).toLong()

        transaction {
            Users.update(User.eqOp(guildId, userId)) {
                it[Users.experienceTotal] = user.experienceTotal + actualGain
            }
        }
    }

}