package nuke.roleplaybot

import nuke.roleplaybot.bot.basic.RoleplayBotBasic
import nuke.roleplaybot.util.Config

@Throws(Exception::class)
fun main(args: Array<String>) {
    RoleplayBotBasic(Config(CONFIG_FILENAME))
}
