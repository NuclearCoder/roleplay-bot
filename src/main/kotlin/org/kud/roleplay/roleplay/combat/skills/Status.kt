package org.kud.roleplay.roleplay.combat.skills

/**
 * Created by NuclearCoder on 2017-06-22.
 */

data class Status(val type: StatusType, val argument: Int = 0) {

    enum class StatusType {
        SLOW, STUN, POISON, BURNING, CHILLED, CONFUSION, SPELL_LOCK, LEACHED

        // marks
        // ...
    }

}