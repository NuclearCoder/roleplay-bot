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
import net.dv8tion.jda.core.entities.VoiceChannel
import net.dv8tion.jda.core.exceptions.PermissionException
import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.music.GuildMusicManager


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

val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()
//private final Map<String, GuildMusicManager> musicManagers;
private val musicManagers: HashMap<String, GuildMusicManager> = HashMap()

class MusicStartCommand(private val audioUrl: String) : Command() {
    init {
        playerManager.registerSourceManager(YoutubeAudioSourceManager())
    }

    override fun onInvoke(context: CommandContext) {
        val mng: GuildMusicManager = getMusicManager(context.event.guild)
        mng.scheduler.isRepeating = true
        context.event.guild.audioManager.sendingHandler = mng.sendHandler

        if (!context.event.member.voiceState.inVoiceChannel()) {
            context.createResponder().fail().setMessage("you're not in a voice channel!").queue()
            return
        }
        val vc: VoiceChannel = context.event.member.voiceState.channel

        try {
            context.event.guild.audioManager.openAudioConnection(vc)
        } catch (e: PermissionException) {
            if (e.permission == Permission.VOICE_CONNECT) {
                context.createResponder().fail().setMessage("I don't have permission to connect to your voice channel.").queue()
                return
            }
        }
        loadAndPlay(getMusicManager(context.event.guild), context, audioUrl)
    }
}

private fun loadAndPlay(mng: GuildMusicManager, context: CommandContext, trackUrl: String) {
    playerManager.loadItemOrdered(mng, trackUrl, object : AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack) {
            mng.scheduler.queue(track)
            context.createResponder().success().setMessage("roleplay music started!").queue()
        }

        override fun playlistLoaded(playlist: AudioPlaylist) {
            // DO NOT USE PLAYLISTS PLEASE
            var firstTrack: AudioTrack? = playlist.selectedTrack
            val tracks = playlist.tracks


            if (firstTrack == null) {
                firstTrack = playlist.tracks[0]
            }
        }

        override fun noMatches() {

        }

        override fun loadFailed(exception: FriendlyException) {
            context.createResponder().fail().setMessage("an error occurred loading the roleplay music!").queue()
        }
    })
}

fun getMusicManager(guild: Guild): GuildMusicManager {
    val guildId = guild.id
    var mng: GuildMusicManager? = musicManagers[guildId]
    if (mng == null) {
        synchronized(musicManagers) {
            mng = musicManagers[guildId]
            if (mng == null) {
                val newmng = GuildMusicManager(playerManager)
                newmng.player.volume = 100
                musicManagers.put(guildId, newmng)
                return newmng
            }
        }
    }
    return mng!!
}