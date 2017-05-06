package org.kud.roleplay.command.music

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.exceptions.PermissionException
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command


/**
Created by Chocolate on 5/05/17.

Copyright (c) 2017 Chocolate.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

class MusicStartCommand(private val audioUrl: String) : Command() {

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
        context.bot.audio.loadAndPlay(
                context.bot.audio.getMusicManager(context.event.guild),
                context, audioUrl)
    }


}
