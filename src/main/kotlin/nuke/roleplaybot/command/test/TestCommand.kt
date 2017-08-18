package nuke.roleplaybot.command.test

import club.minnced.kjda.plusAssign
import nuke.roleplaybot.LOGGER
import nuke.roleplaybot.command.meta.CommandContext
import nuke.roleplaybot.command.meta.command.Command
import nuke.roleplaybot.roleplay.abilities.Abilities

object TestCommand : Command() {
    override fun onInvoke(context: CommandContext) {
        LOGGER.info("Test command.")

        context.reply {
            this += '\n'
            Abilities.byStringId.forEach { _, a ->
                this += a.toString()
                this += "\n"
            }
        }
    }
}