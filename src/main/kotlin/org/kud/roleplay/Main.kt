package org.kud.roleplay

import org.kud.roleplay.util.Config
import sx.blah.discord.modules.Configuration

@Throws(Exception::class)
fun main(args: Array<String>) {
    Configuration.LOAD_EXTERNAL_MODULES = false
    Configuration.AUTOMATICALLY_ENABLE_MODULES = false

    RoleplayBot(Config()).login()
}