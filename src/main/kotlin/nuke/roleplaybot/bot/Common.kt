package nuke.roleplaybot.bot

import club.minnced.kjda.client
import club.minnced.kjda.plusAssign
import club.minnced.kjda.token
import com.mashape.unirest.http.Unirest
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.hooks.AnnotatedEventManager
import net.dv8tion.jda.core.requests.Requester
import nuke.roleplaybot.command.admin.ExitCommand
import nuke.roleplaybot.command.meta.CommandService
import nuke.roleplaybot.command.music.MusicStartCommand
import nuke.roleplaybot.command.music.MusicStopCommand
import nuke.roleplaybot.command.roleplay.characters.CharaCreateCommand
import nuke.roleplaybot.command.roleplay.characters.CharaDeleteCommand
import nuke.roleplaybot.command.roleplay.characters.CharaInfoCommand
import nuke.roleplaybot.command.roleplay.characters.CharaListCommand
import nuke.roleplaybot.command.roleplay.characters.CharaSelectCommand
import nuke.roleplaybot.command.roleplay.characters.CharaUpdateCommand
import nuke.roleplaybot.command.roleplay.combat.CombatStartCommand
import nuke.roleplaybot.command.roleplay.combat.CombatStopCommand
import nuke.roleplaybot.command.roleplay.experience.ExperienceLeaderboardCommand
import nuke.roleplaybot.command.roleplay.experience.ExperienceShowCommand
import nuke.roleplaybot.command.test.TestCommand

internal fun RoleplayBot.buildCommands(instance: RoleplayBot) = CommandService(instance) {
    it["stop"] = ExitCommand
    it["test"] = TestCommand

    it("music") {
        it["start"] = MusicStartCommand
        it["stop"] = MusicStopCommand
    }

    it("chara") {
        it["create"] = CharaCreateCommand
        it["delete"] = CharaDeleteCommand
        it["update"] = CharaUpdateCommand
        it["list"] = CharaListCommand
        it["info"] = CharaInfoCommand
        it["select"] = CharaSelectCommand
    }

    it("battle") {
        it["start"] = CombatStartCommand
        it["stop"] = CombatStopCommand
    }

    it["exp"] = ExperienceShowCommand
    it["top"] = ExperienceLeaderboardCommand
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
