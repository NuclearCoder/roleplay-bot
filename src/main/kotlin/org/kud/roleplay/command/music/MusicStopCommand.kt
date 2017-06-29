package org.kud.roleplay.command.music

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

object MusicStopCommand : Command() {

    override fun onInvoke(context: CommandContext) {
        context.bot.audio.getMusicManager(context.event.guild).run {
            context.event.guild.audioManager.closeAudioConnection()
            this.player.destroy()
        }
        context.reply("roleplay music stopped.")
    }
}