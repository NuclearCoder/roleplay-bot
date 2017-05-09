package org.kud.roleplay.command.roleplay.characters

class CharaCommand : org.kud.roleplay.command.meta.command.Command() {

    // FIXME! emotes fuck up the database; should remove any from the messages

    override fun onInvoke(context: org.kud.roleplay.command.meta.CommandContext) {
        context.replyFail("you haven't specified a valid sub-command.\n```\ncreate | delete | update```")
    }
}