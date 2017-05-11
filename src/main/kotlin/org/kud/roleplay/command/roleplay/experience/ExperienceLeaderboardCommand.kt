package org.kud.roleplay.command.roleplay.experience

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

class ExperienceLeaderboardCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guild = context.event.guild
        val guildId = guild.idLong

        context.event.channel.send {
            this += "**${guild.name}**"
            this += " :clipboard: leaderboards\n```python\n"
            context.bot.database.getUserLeaderboards(guildId, 10).forEachIndexed {
                index, (_, userId, experience) ->

                val member = guild.getMemberById(userId)
                val name = member?.effectiveName ?: "user left: $userId"

                this += "# "
                this += "${index + 1}.".padEnd(3, ' ')
                this += name
                this += "\n    Experience: ${experience.total}\n"
            }
            this += "```"
        }
    }

}