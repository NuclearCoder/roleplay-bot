package org.kud.roleplay.command.roleplay

import org.kud.roleplay.LOGGER
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.command.Command
import org.kud.roleplay.util.rReply
import sx.blah.discord.handle.obj.IMessage
import java.sql.SQLException

class CharaCommand : Command {

    // FIXME! emotes fuck up the database; should remove any from the messages

    override fun execute(bot: RoleplayBot, message: IMessage, command: String, args: Array<String>) {
        // args: chara, cmd[, args...]
        if (args.size >= 2) {
            val cmd = args[1].toLowerCase()
            when (cmd) {
                "create" -> create(bot, message, args)
                "delete" -> delete(bot, message, args)
                "update" -> update(bot, message, args)
                "list" -> list(bot, message, args)
                "info" -> info(bot, message, args)
                else -> {
                    message.rReply("you haven't specified a valid sub-command. :warning:\n```\ncreate | delete | update```")
                }
            }
        }
    }

    private fun create(bot: RoleplayBot, message: IMessage, args: Array<String>) {
        if (args.size >= 3) {
            val guildId = message.guild.longID
            val userId = message.author.longID

            val name = args.drop(2).joinToString(" ")

            // TODO: sanity check for name

            if (!bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    bot.database.createRoleplayCharacter(guildId, userId, name)
                    message.rReply("your character \"$name\" has been created! :white_check_mark:")
                } catch (e: SQLException) {
                    message.rReply("an error occurred while creating your character. :negative_squared_cross_mark:")
                    LOGGER.error("Could not create character.", e)
                }
            } else {
                message.rReply("you already have a character with that name. :negative_squared_cross_mark:")
            }
        } else {
            message.rReply("you haven't specified a character name. :warning:")
        }
    }

    private fun delete(bot: RoleplayBot, message: IMessage, args: Array<String>) {
        if (args.size >= 3) {
            val guildId = message.guild.longID
            val userId = message.author.longID

            val name = args.drop(2).dropWhile(String::isEmpty).joinToString(" ").replace('\n', ' ')

            if (bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    bot.database.deleteRoleplayCharacter(guildId, userId, name)
                    message.rReply("your character \"$name\" has been deleted! :white_check_mark:")
                } catch (e: SQLException) {
                    message.rReply("an error occurred while deleting your character. :negative_squared_cross_mark:")
                    LOGGER.error("Could not delete character.", e)
                }
            } else {
                message.rReply("you have no character with that name. :negative_squared_cross_mark:")
            }
        } else {
            message.rReply("you haven't specified a character name. :warning:")
        }
    }

    private fun update(bot: RoleplayBot, message: IMessage, args: Array<String>) {
        if (args.size >= 3) {
            val guildId = message.guild.longID
            val userId = message.author.longID

            // the first line break delimits the chara info
            val rest = args.drop(2).joinToString(" ").split("\n".toRegex(), 2)
            val name = rest[0].trim()

            // TODO: maybe sanity check on content

            if (bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                if (rest.size == 2) {
                    val content = rest[1].trim()
                    try {
                        bot.database.updateRoleplayCharacter(guildId, userId, name, content)
                        message.rReply("your character \"$name\" has been updated! :white_check_mark:")
                    } catch (e: SQLException) {
                        message.rReply("an error occurred while updating your character. :negative_squared_cross_mark:")
                        LOGGER.error("Could not update character.", e)
                    }
                } else {
                    message.rReply("you haven't specified a character description/bio. :warning:\nEnter your bio after the first line break. (Shift+ENTER to begin a new line)")
                }
            } else {
                message.rReply("you have no character with that name. :negative_squared_cross_mark:")
            }
        } else {
            message.rReply("you haven't specified a character name. :warning:")
        }
    }

    private fun list(bot: RoleplayBot, message: IMessage, args: Array<String>) {
        val guildId = message.guild.longID
        val userId = (message.mentions.firstOrNull() ?: message.author).longID  // first mention or self list if none

        val characters = bot.database.getRoleplayCharacterList(guildId, userId)

        if (characters.isNotEmpty()) {
            val list = characters.map { it.name }.joinToString(prefix = "```\n", separator = "\n", postfix = "```")
            message.rReply("here are the characters created by this user.\n$list")
        } else {
            message.rReply("this user has no character. :(")
        }
    }

    private fun info(bot: RoleplayBot, message: IMessage, args: Array<String>) {
        if (args.size >= 4) {
            val guild = message.guild
            val user = (message.mentions.firstOrNull() ?: message.author)

            val guildId = guild.longID
            val userId = user.longID

            // TODO: proper check if mention is the 3th argument; for now it's only assumed so.

            val name = args.drop(3).joinToString(" ")

            if (bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    // we checked previously so a null value here would be an error,
                    // do explicitly throw a NPE.
                    val character = bot.database.getRoleplayCharacter(guildId, userId, name)!!

                    message.rReply("here is character info for \"${character.name}\", created by **${user.getDisplayName(message.guild)}** :\n${character.content}")
                } catch (e: SQLException) {
                    message.rReply("an error occurred while fetching character info. :negative_squared_cross_mark:")
                    LOGGER.error("Could not fetch character info.", e)
                }
            } else {
                message.rReply("there was no character with that name. :negative_squared_cross_mark:")
            }
        } else {
            message.rReply("you haven't specified a character name and/or character owner. :warning:")
        }
    }

}