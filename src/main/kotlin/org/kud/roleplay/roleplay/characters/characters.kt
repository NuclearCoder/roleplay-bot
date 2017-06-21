package org.kud.roleplay.roleplay.characters

import org.kud.roleplay.database.Character
import org.kud.roleplay.database.Item
import kotlin.reflect.KProperty1

/**
 * Created by NuclearCoder on 21/06/17.
 */

private val itemFields = listOf(
        Character::weapon1, Character::weapon2, Character::weapon3, Character::weapon4,
        Character::shield,
        Character::helmet, Character::chestplate, Character::leggings, Character::boots,
        Character::amulet, Character::ring,
        Character::consumable1, Character::consumable2
)

// equivalent to `weapon1.prop + weapon2.prop + ...`
fun Character.totalItemBonusFor(property: KProperty1<Item, Int>) =
        itemFields.map { item -> property(item(this)) }.sum()

val Character.health get() = statHealth + totalItemBonusFor(Item::health)
val Character.mana get() = statMana + totalItemBonusFor(Item::mana)
val Character.stamina get() = statStamina + totalItemBonusFor(Item::stamina)
val Character.strength get() = statStrength + totalItemBonusFor(Item::strength)
val Character.magic get() = statMagic + totalItemBonusFor(Item::magic)
val Character.defense get() = statDefense + totalItemBonusFor(Item::defense)
val Character.resistance get() = statResistance + totalItemBonusFor(Item::resistance)
val Character.speed get() = statSpeed + totalItemBonusFor(Item::speed)
val Character.accuracy get() = statAccuracy + totalItemBonusFor(Item::accuracy)
val Character.skill get() = statSkill + totalItemBonusFor(Item::skill)
