package org.kud.roleplay.command.roleplay

import org.kud.roleplay.LOGGER
import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext
import java.sql.SQLException

class CharaCommand : Command() {

    // FIXME! emotes fuck up the database; should remove any from the messages

    override fun onInvoke(context: CommandContext) {
        // args: chara, cmd[, args...]
        if (context.args.size >= 2) {
            val cmd = context.args[1].toLowerCase()
            when (cmd) { // FIXME sub-commands be handled by the command service
                "create" -> create(context)
                "delete" -> delete(context)
                "update" -> update(context)
                "list" -> list(context)
                "info" -> info(context)
                else -> {
                    context.replyFail("you haven't specified a valid sub-command.\n```\ncreate | delete | update```")
                }
            }
        }
    }

    private fun create(context: CommandContext) {
        if (context.args.size >= 3) {
            val guildId = context.message.guild.idLong
            val userId = context.message.author.idLong

            val name = context.args.drop(2).joinToString(" ")

            // TODO: sanity check for name

            if (!context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    context.bot.database.createRoleplayCharacter(guildId, userId, name)
                    context.replySuccess("your character \"$name\" has been created!")
                } catch (e: SQLException) {
                    LOGGER.error("Could not create character.", e)
                    context.replyFail("an error occurred while creating your character.")
                }
            } else {
                context.reply {
                    fail()
                    setMessage("you already have a character with that name.")
                }
            }
        } else {
            context.reply {
                fail()
                setMessage("you haven't specified a character name.")
            }
        }
    }

    private fun delete(context: CommandContext) {
        if (context.args.size >= 3) {
            val guildId = context.message.guild.idLong
            val userId = context.message.author.idLong

            val name = context.args.drop(2).dropWhile(String::isEmpty).joinToString(" ").replace('\n', ' ')

            if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    context.bot.database.deleteRoleplayCharacter(guildId, userId, name)
                    context.reply {
                        setMessage("your character \"$name\" has been deleted!")
                    }
                } catch (e: SQLException) {
                    LOGGER.error("Could not delete character.", e)
                    context.reply {
                        fail()
                        setMessage("an error occurred while deleting your character.")
                    }
                }
            } else {
                context.reply {
                    fail()
                    setMessage("you have no character with that name.")
                }
            }
        } else {
            context.reply {
                fail()
                setMessage("you haven't specified a character name.")
            }
        }
    }

    private fun update(context: CommandContext) {
        if (context.args.size >= 3) {
            val guildId = context.message.guild.idLong
            val userId = context.message.author.idLong

            // the first line break delimits the chara info
            val rest = context.args.drop(2).joinToString(" ").split("\n".toRegex(), 2)
            val name = rest[0].trim()

            // TODO: maybe sanity check on content

            if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                if (rest.size == 2) {
                    val content = rest[1].trim()
                    try {
                        context.bot.database.updateRoleplayCharacter(guildId, userId, name, content)
                        context.reply {
                            setMessage("your character \"$name\" has been updated!")
                        }
                    } catch (e: SQLException) {
                        LOGGER.error("Could not update character.", e)
                        context.reply {
                            fail()
                            setMessage("an error occurred while updating your character.")
                        }
                    }
                } else {
                    context.reply {
                        fail()
                        setMessage("you haven't specified a character description/bio.\nEnter your bio after the first line break. (Shift+ENTER to begin a new line)")
                    }
                }
            } else {
                context.reply {
                    fail()
                    setMessage("you have no character with that name.")
                }
            }
        } else {
            context.reply {
                fail()
                setMessage("you haven't specified a character name.")
            }
        }
    }

    private fun list(context: CommandContext) {
        val guildId = context.event.guild.idLong
        val userId = (context.message.mentionedUsers.firstOrNull() ?: context.event.author).idLong  // first mention or self list if none

        val characters = context.bot.database.getRoleplayCharacterList(guildId, userId)

        if (characters.isNotEmpty()) {
            val list = characters.map { it.name }.joinToString(prefix = "```\n", separator = "\n", postfix = "```")
            context.reply {
                setMessage("here are the characters created by this user.\n$list")
            }
        } else {
            context.reply {
                fail()
                setMessage("this user has no character.")
            }
        }
    }

    private fun info(context: CommandContext) {
        if (context.args.size >= 4) {
            val guild = context.event.guild
            val user = (context.message.mentionedUsers.firstOrNull() ?: context.event.author)

            val guildId = guild.idLong
            val userId = user.idLong

            // TODO: proper check if mention is the 3th argument; for now it's only assumed so.

            val name = context.args.drop(3).joinToString(" ")

            if (context.bot.database.existsRoleplayCharacter(guildId, userId, name)) {
                try {
                    // we checked previously so a null value here would be an error,
                    // do explicitly throw a NPE.
                    val character = context.bot.database.getRoleplayCharacter(guildId, userId, name)!!
                    context.reply {
                        setMessage("here is character info for \"${character.name}\", created by **${guild.getMember(user).effectiveName}** :\n${character.content}")
                    }
                } catch (e: SQLException) {
                    LOGGER.error("Could not fetch character info.", e)
                    context.reply {
                        fail()
                        setMessage("an error occurred while fetching character info.")
                    }
                }
            } else {
                context.reply {
                    fail()
                    setMessage("there was no character with that name.")
                }
            }
        } else {
            context.reply {
                fail()
                setMessage("you haven't specified a character name and/or character owner.")
            }
        }
    }

}