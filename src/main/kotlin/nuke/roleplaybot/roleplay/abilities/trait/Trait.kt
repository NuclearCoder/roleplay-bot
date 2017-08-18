package nuke.roleplaybot.roleplay.abilities.trait

import nuke.roleplaybot.database.Items.ItemType
import nuke.roleplaybot.roleplay.abilities.Status.StatusType

sealed class Trait {

    // active-only traits
    abstract class Active : Trait()

    data class Throw(val range: Int) : Active() // throw and lose item
    data class Throwre(val range: Int) : Active() // throw and get it back
    data class Damage(val range: Int, val amount: Int) : Active() // range 1 = melee

    // passive-only traits
    abstract class Passive : Trait()

    data class Slot(val slot: ItemType) : Passive()

    // general traits
    data class SelfStatus(val status: StatusType, val chance: Int) : Trait()
    data class Status(val status: StatusType, val chance: Int, val isActive: Boolean) : Trait()




}