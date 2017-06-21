package org.kud.roleplay.command.roleplay.experience

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.User
import org.kud.roleplay.roleplay.experience.getLevelValue

object ExperienceShowCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = context.tokenizer.nextUserMention() ?: context.event.author.idLong

        val member = context.event.guild.getMemberById(userId)

        transaction {
            val user = User.getOrInit(guildId, userId)

            context.event.channel.send {
                this += "**${member.effectiveName}**"
                this += " :sparkles: level **${user.experienceLevel}**\n"
                this += "Level completion: **${user.experienceRemaining}**/**${getLevelValue(user.experienceLevel)}**\n"
                this += "Total experience: **${user.experienceTotal}**"
            }
        }
    }

}