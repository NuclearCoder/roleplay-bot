package org.kud.roleplay

import org.kud.roleplay.bot.basic.RoleplayBotBasic
import org.kud.roleplay.util.Config

@Throws(Exception::class)
fun main(args: Array<String>) {
    RoleplayBotBasic(Config(CONFIG_FILENAME))
}