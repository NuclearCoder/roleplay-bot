package org.kud.roleplay.command

import sx.blah.discord.api.events.IListener
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

interface CommandManager : IListener<MessageReceivedEvent> {

    override fun handle(event: MessageReceivedEvent)

}