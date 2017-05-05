package org.kud.roleplay

import org.kud.roleplay.util.Config

@Throws(Exception::class)
fun main(args: Array<String>) {
    RoleplayBot(Config(CONFIG_FILENAME))
}