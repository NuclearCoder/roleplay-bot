package org.kud.roleplay.command.roleplay.experience

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.roleplay.experience.getLevelValue

class ExperienceShowCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = context.tokenizer.nextUserMention() ?: context.event.author.idLong

        val member = context.event.guild.getMemberById(userId)

        val experience = context.bot.users[guildId, userId].experience

        context.event.channel.send {
            this += "**${member.effectiveName}**"
            this += " :sparkles: level **${experience.level}**\n"
            this += "Level completion: **${experience.levelXP}**/**${getLevelValue(experience.level)}**\n"
            this += "Total experience: **${experience.total}**"
        }
    }

}