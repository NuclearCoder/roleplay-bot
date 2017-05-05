package org.kud.roleplay.command.meta

import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.MessageEmbed
import java.io.InputStream
import java.util.*

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

    private var content = Optional.empty<String>()
    private var emote = ":white_check_mark:"

    private var embed = Optional.empty<MessageEmbed>()

    private var embedMessage: EmbedBuilder? = null
    private var embedMessageMessage = Optional.empty<String>()

    private var stream: InputStream? = null
    private var filename = Optional.empty<String>()

    fun setEmbed(embed: MessageEmbed) {
        this.embed = Optional.of(embed)
    }

    fun setEmbed(embed: EmbedBuilder) {
        this.embed = Optional.of(embed.build())
    }

    fun success() {
        this.emote = ":white_check_mark:"
    }

    fun fail() {
        this.emote = ":negative_squared_cross_mark:"
    }

    fun setEmote(emote: String) {
        this.emote = emote
    }

    fun setMessage(message: String) {
        this.content = Optional.of(message)
    }

    fun setEmbedMessage(embed: EmbedBuilder, message: String) {
        this.embedMessage = embed
        this.embedMessageMessage = Optional.of(message)
    }

    fun attachFile(stream: InputStream, filename: String) {
        this.stream = stream
        this.filename = Optional.of(filename)
    }

    fun missingArguments(details: String) {
        this.fail()
        this.setMessage("missing arguments: " + details)
    }

    // TO-DO: Use message builders

    private fun queueInternal(message: Message) {
        filename.ifPresent {
            context.event.channel.sendFile(stream, it, message).queue()
        }
    }

    fun queue() {
        val rootBuilder = MessageBuilder()

        content.ifPresent {
            rootBuilder.append(emote).append(" | **").append(context.event.member.effectiveName).append("**, ").append(it)
        }

        embed.ifPresent {
            context.event.channel.sendMessage(it).queue()
            rootBuilder.setEmbed(it)
        }

        embedMessageMessage.ifPresent {
            context.event.channel.sendMessage(embedMessage!!.setDescription(emote + " | **" + context.event.member.effectiveName + "**, " + it).build()).queue()
        }

        if (filename.isPresent) {
            queueInternal(rootBuilder.build())
        } else {
            context.event.channel.sendMessage(rootBuilder.build()).queue()
        }
    }

    fun queueRaw() {
        content.ifPresent {
            context.event.channel.sendMessage(it).queue(this::queueInternal)
        }
        embed.ifPresent {
            context.event.channel.sendMessage(it).queue()
        }
        embedMessageMessage.ifPresent {
            context.event.channel.sendMessage(embedMessage!!.setDescription(it).build()).queue()
        }
    }
}
