package org.kud.roleplay.bot

import club.minnced.kjda.client
import club.minnced.kjda.plusAssign
import club.minnced.kjda.token
import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.hooks.AnnotatedEventManager
import net.dv8tion.jda.core.requests.Requester
import org.kud.roleplay.command.admin.ExitCommand
import org.kud.roleplay.command.meta.CommandService
import org.kud.roleplay.command.music.MusicStartCommand
import org.kud.roleplay.command.music.MusicStopCommand
import org.kud.roleplay.command.roleplay.characters.CharaCreateCommand
import org.kud.roleplay.command.roleplay.characters.CharaDeleteCommand
import org.kud.roleplay.command.roleplay.characters.CharaInfoCommand
import org.kud.roleplay.command.roleplay.characters.CharaListCommand
import org.kud.roleplay.command.roleplay.characters.CharaSelectCommand
import org.kud.roleplay.command.roleplay.characters.CharaUpdateCommand
import org.kud.roleplay.command.roleplay.combat.CombatStartCommand
import org.kud.roleplay.command.roleplay.experience.ExperienceLeaderboardCommand
import org.kud.roleplay.command.roleplay.experience.ExperienceShowCommand
import org.kud.roleplay.command.test.TestCommand

/**
 * Created by NuclearCoder on 5/20/2017.
 */

internal fun RoleplayBot.buildCommands(instance: RoleplayBot) = CommandService(instance) {
    register("stop", ExitCommand)
    register("test", TestCommand)

    register("music") {
        register("start", MusicStartCommand)
        register("stop", MusicStopCommand)
    }

    register("chara") {
        register("create", CharaCreateCommand)
        register("delete", CharaDeleteCommand)
        register("update", CharaUpdateCommand)
        register("list", CharaListCommand)
        register("info", CharaInfoCommand)
        register("select", CharaSelectCommand)
    }

    register("battle") {
        register("start", CombatStartCommand)
    }

    register("exp", ExperienceShowCommand)
    register("top", ExperienceLeaderboardCommand)
}

internal fun RoleplayBot.getRecommendedShardCount() =
        Unirest.get(Requester.DISCORD_API_PREFIX + "gateway/bot")
                .header("Authorization", "Bot ${config["token"]}")
                .header("User-agent", Requester.USER_AGENT)
                .asJson().body.`object`.getInt("shards")

internal fun RoleplayBot.buildClient(preInit: JDABuilder.() -> Unit = {}): JDA = client(AccountType.BOT) {
    token { config["token"] }

    preInit()

    setEventManager(AnnotatedEventManager())

    this += commands
}
