package nuke.roleplaybot.command.roleplay.experience

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.Command
import nuke.roleplaybot.database.User
import nuke.roleplaybot.roleplay.experience.getLevelValue
import org.jetbrains.exposed.sql.transactions.transaction

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