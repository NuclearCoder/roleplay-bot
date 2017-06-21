package org.kud.roleplay.command.roleplay.experience

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command
import org.kud.roleplay.database.Users

object ExperienceLeaderboardCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        val guild = context.event.guild
        val guildId = guild.idLong

        context.event.channel.send {
            this += "**${guild.name}**"
            this += " :clipboard: leaderboards\n```python\n"

            transaction {
                Users.select { Users.guildId eq guildId }
                        .orderBy(Users.experienceTotal, isAsc = false).limit(10).forEachIndexed {
                    index, row ->

                    val userId = row[Users.userId]
                    val experience = row[Users.experienceTotal]

                    val member = guild.getMemberById(userId)
                    val name = member?.effectiveName ?: "user left (ID: $userId)"

                    this@send += "# "
                    this@send += "${index + 1}.".padEnd(3, ' ')
                    this@send += name
                    this@send += "\n    Experience: $experience\n"
                }
            }

            this += "```"
        }
    }

}