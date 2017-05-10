package org.kud.roleplay.command.meta

import club.minnced.kjda.entities.send
import club.minnced.kjda.plusAssign
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.kud.roleplay.RoleplayBot
import org.kud.roleplay.util.MessageTokenizer

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

class CommandContext(val event: MessageReceivedEvent,
                     val bot: RoleplayBot,
                     val message: Message,
                     val command: String,
                     val tokenizer: MessageTokenizer) {

    inline fun reply(emote: String = REPLY_SUCCESS, crossinline then: MessageBuilder.() -> Unit) {
        event.channel.send {
            replyPrefix(event.member, emote)
            then()
        }
    }

    fun reply(emote: String, content: String) = reply(emote) { this += content }

    fun reply(content: String) = reply(REPLY_SUCCESS, content)
    fun replyFail(content: String) = reply(REPLY_FAILURE, content)

    fun replyMissingArguments(details: String) = reply(REPLY_FAILURE) {
        this += "missing arguments: "
        this += details
    }

}