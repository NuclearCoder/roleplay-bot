package org.kud.roleplay.command.music

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.exceptions.PermissionException
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

object MusicStartCommand : Command() {

    var audioUrl = "https://www.youtube.com/watch?v=zJvhDfYU_LU"

    override fun onInvoke(context: CommandContext) {
        context.bot.audio.getMusicManager(context.event.guild).run {
            this.scheduler.isRepeating = true
            context.event.guild.audioManager.sendingHandler = this.sendHandler
        }

        if (!context.event.member.voiceState.inVoiceChannel()) {
            context.replyFail("you're not in a voice channel!")
            return
        }

        try {
            val voiceChannel = context.event.member.voiceState.channel
            context.event.guild.audioManager.openAudioConnection(voiceChannel)
        } catch (e: PermissionException) {
            if (e.permission == Permission.VOICE_CONNECT) {
                context.replyFail("I don't have permission to connect to your voice channel.")
            }
        }
        context.bot.audio.loadAndPlay(context.bot.audio.getMusicManager(context.event.guild), context, audioUrl)
    }


}
