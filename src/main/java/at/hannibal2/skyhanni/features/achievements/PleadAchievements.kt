package at.hannibal2.skyhanni.features.achievements

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.achievements.Achievement
import at.hannibal2.skyhanni.data.hypixel.chat.event.PlayerAllChatEvent
import at.hannibal2.skyhanni.events.achievements.AchievementRegistrationEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.PlayerUtils
import at.hannibal2.skyhanni.utils.chat.TextHelper
import at.hannibal2.skyhanni.utils.compat.append
import at.hannibal2.skyhanni.utils.compat.componentBuilder
import at.hannibal2.skyhanni.utils.compat.withColor
import net.minecraft.ChatFormatting

@SkyHanniModule
object PleadAchievements {

    private val pleadComponent = TextHelper.createAtlasSprite("plead")
    private val pleadDyeComponent = TextHelper.createAtlasSprite("pleaddye")

    private const val PLEAD_ACHIEVEMENT = "Plead"
    private const val PLEAD_DYE_ACHIEVEMENT = "Plead Dye"

    @HandleEvent
    fun onAchievementRegistration(event: AchievementRegistrationEvent) {
        val pleadAchievement = Achievement(
            pleadComponent,
            pleadComponent,
            secret = true
        )
        val pleadDyeAchievement = Achievement(
            componentBuilder {
                append("Plead Dye ") {
                    withColor(ChatFormatting.YELLOW)
                }
                append(pleadDyeComponent)
            },
            componentBuilder {
                append("You found a Plead Dye!") {
                    withColor(ChatFormatting.YELLOW)
                }
                append("\n")
                append("A pleadful 1/10,000 (0.01%) chance! ") {
                    withColor(ChatFormatting.LIGHT_PURPLE)
                }
                append(pleadDyeComponent)
            },
            secret = true
        )
        event.register(pleadAchievement, PLEAD_ACHIEVEMENT)
        event.register(pleadDyeAchievement, PLEAD_DYE_ACHIEVEMENT)
    }

    @HandleEvent(onlyOnSkyblock = true)
    fun onChat(event: PlayerAllChatEvent.Allow) {
        if (!event.author.contains(PlayerUtils.getName())) return
        if (!event.cleanMessage.contains("plead")) return
        AchievementManager.completeAchievement(PLEAD_ACHIEVEMENT)
        val pleadRoll = (1..10_000).random()
        if (pleadRoll != 1) return
        AchievementManager.completeAchievement(PLEAD_DYE_ACHIEVEMENT)
    }
}
