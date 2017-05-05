package org.kud.roleplay.command.manage

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.exceptions.PermissionException
import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.NoRoleplayRole
import org.kud.roleplay.music.GuildMusicManager
import java.util.concurrent.ConcurrentHashMap


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

//FIXME yuck, global state

val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()
private val musicManagers = ConcurrentHashMap<String, GuildMusicManager>()

@NoRoleplayRole
class MusicStartCommand(private val audioUrl: String) : Command() {
    init {
        playerManager.registerSourceManager(YoutubeAudioSourceManager())
    }

    override fun onInvoke(context: CommandContext) {
        getMusicManager(context.event.guild).run {
            this.scheduler.isRepeating = true
            context.event.guild.audioManager.sendingHandler = this.sendHandler
        }

        if (!context.event.member.voiceState.inVoiceChannel()) {
            context.reply {
                fail()
                setMessage("you're not in a voice channel!")
            }
            return
        }

        try {
            val voiceChannel = context.event.member.voiceState.channel
            context.event.guild.audioManager.openAudioConnection(voiceChannel)
        } catch (e: PermissionException) {
            if (e.permission == Permission.VOICE_CONNECT) {
                context.reply {
                    fail()
                    setMessage("I don't have permission to connect to your voice channel.")
                }
            }
        }
        loadAndPlay(getMusicManager(context.event.guild), context, audioUrl)
    }
}

private fun loadAndPlay(manager: GuildMusicManager, context: CommandContext, trackUrl: String) {
    playerManager.loadItemOrdered(manager, trackUrl, object : AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack) {
            manager.scheduler.queue(track)
            context.reply {
                success()
                setMessage("roleplay music started!")
            }
        }

        override fun playlistLoaded(playlist: AudioPlaylist) {
            // DO NOT USE PLAYLISTS PLEASE
            val tracks = playlist.tracks
            var firstTrack = playlist.selectedTrack ?: tracks[0]
        }

        override fun noMatches() {

        }

        override fun loadFailed(exception: FriendlyException) {
            context.reply {
                fail()
                setMessage("an error occurred loading the roleplay music!")
            }
        }
    })
}

fun getMusicManager(guild: Guild): GuildMusicManager {
    val guildId = guild.id
    return musicManagers.computeIfAbsent(guildId) {
        GuildMusicManager(playerManager).apply { player.volume = 100 }
    }
}