package org.kud.roleplay

import org.kud.roleplay.command.CommandManager
import org.kud.roleplay.command.CommandManagerImpl
import org.kud.roleplay.database.Database
import org.kud.roleplay.util.Config
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.ReadyEvent
import sx.blah.discord.util.DiscordException
import sx.blah.discord.util.RequestBuffer

class RoleplayBot(private val config: Config) : IListener<ReadyEvent> {

    private val keeper = TimerKeepAlive()

    val client: IDiscordClient = ClientBuilder().withToken(config["token"]).setMaxReconnectAttempts(Int.MAX_VALUE).build()

    val commands: CommandManager = CommandManagerImpl(this)

    val database = Database(config)

    init {
        client.dispatcher.registerListener(this)
    }

    fun login() {
        LOGGER.info("Logging bot...")

        RequestBuffer.request {
            try {
                client.login()
            } catch (e: DiscordException) {
                LOGGER.error("Could not log in.", e)
            }
        }
    }

    fun terminate() {
        LOGGER.info("Disconnecting bot...")
        try {
            client.logout()

            database.disconnect()
            config.save()

            keeper.alive.set(false)
        } catch (e: DiscordException) {
            LOGGER.error("Could not log out.", e)
        }
    }

    override fun handle(event: ReadyEvent) {
        client.dispatcher.registerListener(commands)

        LOGGER.info("*** Bot is ready! ***")
    }

}
