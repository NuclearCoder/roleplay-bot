package org.kud.roleplay

import sx.blah.discord.modules.Configuration

@Throws(Exception::class)
fun main(args: Array<String>) {
    if (args.isEmpty())
        throw IllegalArgumentException("Must provide client token")

    Configuration.LOAD_EXTERNAL_MODULES = false
    Configuration.AUTOMATICALLY_ENABLE_MODULES = false

    val bot = SimpleBot(args[0])
    bot.login()
}