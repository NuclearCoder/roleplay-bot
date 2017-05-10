package org.kud.roleplay.command.roleplay.characters

import org.kud.roleplay.command.meta.CommandContext
import org.kud.roleplay.command.meta.command.Command

class CharaCommand : Command() {

    // FIXME! emotes fuck up the database; should remove any from the messages

    override fun onInvoke(context: CommandContext) {
        context.replyFail("you haven't specified a valid sub-command.\n```\ncreate | delete | update | list | info```")
        // TODO: generate the command list from registry
    }
}