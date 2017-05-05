package org.kud.roleplay

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.test.TestCommand
import org.kud.roleplay.database.Database
import org.kud.roleplay.util.Config

class RoleplayBot(private val config: Config) {

    private val keeper = TimerKeepAlive()

    var commands = CommandService(this)

    //val client: IDiscordClient = ClientBuilder().withToken(config["token"]).setMaxReconnectAttempts(Int.MAX_VALUE).build()
    val client: JDA = JDABuilder(AccountType.BOT).setToken(config["token"]).buildBlocking()

    init {
        commands = CommandService(this)
        // Register commands here
        commands.register("test", TestCommand())
        client.addEventListener(commands)
    }

    val database = Database(config)

    fun terminate() {
        LOGGER.info("Disconnecting bot...")
        client.shutdown()

            database.disconnect()
            config.save()

            keeper.alive.set(false)
    }

}
