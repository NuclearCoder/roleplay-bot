package org.kud.roleplay.command.meta

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
//bot: RoleplayBot, message: IMessage, command: String, args: Array<String>
class CommandContext(val event: MessageReceivedEvent,
                     val bot: RoleplayBot,
                     val message: Message,
                     val command: String,
                     val tokenizer: MessageTokenizer) {

    /*fun createResponder(): CommandResponder {
        return CommandResponder(this)
    }*/

    fun reply(init: CommandResponder.() -> Unit) {
        val cmdr = CommandResponder(this)
        cmdr.init()
        cmdr.queue()
    }

    fun replySuccess(message: String, and: CommandResponder.() -> Unit = {}) {
        reply {
            setMessage(message)
            and()
        }
    }

    fun replyFail(message: String, and: CommandResponder.() -> Unit = {}) {
        reply {
            setMessage(message)
            fail()
            and()
        }
    }

}