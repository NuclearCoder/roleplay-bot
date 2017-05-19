package org.kud.roleplay.bot.basic

import club.minnced.kjda.client
import club.minnced.kjda.plusAssign
import club.minnced.kjda.token
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.ShutdownEvent
import net.dv8tion.jda.core.hooks.AnnotatedEventManager
import net.dv8tion.jda.core.hooks.SubscribeEvent
import org.kud.roleplay.LOGGER
import org.kud.roleplay.SHUTDOWN_WAIT
import org.kud.roleplay.bot.RoleplayBotBase
import org.kud.roleplay.util.Config

class RoleplayBotBasic(config: Config) : RoleplayBotBase(config) {

    override val commands = buildCommands(this)

    override val client = client(AccountType.BOT) {
        token { config["token"] }

        setEventManager(AnnotatedEventManager())

        this += object {
            @SubscribeEvent
            fun onReady(event: ReadyEvent) {
                commands.initAfterAttach()
            }

            @SubscribeEvent
            fun onShutdown(event: ShutdownEvent) {
                config.save()
            }
        }

        this += commands
    }

    override fun terminate() {
        LOGGER.info("Waiting for last requests...")

        try {
            //wait for requests for an arbitrary time
            Thread.sleep(SHUTDOWN_WAIT)
        } catch (ignored: InterruptedException) {
        }

        LOGGER.info("Shutting down...")

        client.shutdown()
    }


}