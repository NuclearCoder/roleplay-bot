package org.kud.roleplay.command.manage

import org.kud.roleplay.command.meta.Command
import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.PermissionLevel
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
class MusicEndCommand : Command() {
    override var requiredPermission: PermissionLevel = PermissionLevel.Moderator
    override fun onInvoke(context: CommandContext) {
        val mm: GuildMusicManager = getMusicManager(context.event.guild)
        context.event.guild.audioManager.closeAudioConnection()
        mm.player.destroy()
        context.createResponder().success().setMessage("roleplay music stopped.").queue()
    }
}