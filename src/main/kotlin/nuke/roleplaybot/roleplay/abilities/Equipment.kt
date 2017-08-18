package nuke.roleplaybot.roleplay.abilities

enum class Equipment(val max: Int = 1) {

    WEAPON(4), SHIELD,
    HELMET, CHESTPLATE, LEGGINGS, BOOTS,
    AMULET, RING, CONSUMABLE(2)

}