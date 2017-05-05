package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import java.io.InputStream

/**
 * Created by Chocolate on 18/04/17.
 *
 *
 * Copyright (c) 2017 Chocolate.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
class CommandResponder internal constructor(private val context: CommandContext) {
    private var content: String? = null
    private var emote = ":white_check_mark:"
    private var embed: MessageEmbed? = null
    private var embedMessage: EmbedBuilder? = null
    private var embedMessageMessage: String? = null
    private var stream: InputStream? = null
    private var filename: String? = null

    fun setEmbed(embed: MessageEmbed): CommandResponder {
        this.embed = embed
        return this
    }

    fun setEmbed(embed: EmbedBuilder): CommandResponder {
        this.embed = embed.build()
        return this
    }

    fun success(): CommandResponder {
        this.emote = ":white_check_mark:"
        return this
    }

    fun fail(): CommandResponder {
        this.emote = ":negative_squared_cross_mark:"
        return this
    }

    fun setEmote(emote: String): CommandResponder {
        this.emote = emote
        return this
    }

    fun setMessage(message: String): CommandResponder {
        this.content = message
        return this
    }

    fun setEmbedMessage(embed: EmbedBuilder, message: String): CommandResponder {
        this.embedMessage = embed
        this.embedMessageMessage = message
        return this
    }

    fun attachFile(stream: InputStream, filename: String): CommandResponder {
        this.stream = stream
        this.filename = filename
        return this
    }

    fun missingArguments(details: String): CommandResponder {
        return this.fail().setMessage("missing arguments: " + details)
    }

    // TO-DO: Use message builders

    private fun queueInternal(message: Message) {
        if (this.stream != null && this.filename != null) {
            context.event.channel.sendFile(stream!!, filename!!, message).queue()
        }
    }

    fun queue() {
        val rootBuilder = MessageBuilder()
        if (content != null) {
            rootBuilder.append(emote).append(" | **").append(context.event.member.effectiveName).append("**, ").append(content)
        }
        if (embed != null) {
            context.event.channel.sendMessage(embed!!).queue()
            rootBuilder.setEmbed(embed)
        }
        if (embedMessage != null && embedMessageMessage != null) {
            context.event.channel.sendMessage(embedMessage!!.setDescription(emote + " | **" + context.event.member.effectiveName + "**, " + embedMessageMessage).build()).queue()
        }
        if (this.stream != null && this.filename != null)
            queueInternal(rootBuilder.build())
        else
            context.event.channel.sendMessage(rootBuilder.build()).queue()
    }

    fun queueRaw() {
        if (content != null) {
            context.event.channel.sendMessage(content!!).queue({ this.queueInternal(it) })
        }
        if (embed != null) {
            context.event.channel.sendMessage(embed!!).queue()
        }
        if (embedMessage != null && embedMessageMessage != null) {
            context.event.channel.sendMessage(embedMessage!!.setDescription(embedMessageMessage).build()).queue()
        }
    }
}
