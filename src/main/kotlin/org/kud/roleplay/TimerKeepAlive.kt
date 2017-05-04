package org.kud.roleplay

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class TimerKeepAlive : TimerTask() {

    internal val alive = AtomicBoolean(true)

    private val timer = Timer("keep-alive", false)

    init {
        timer.schedule(this, BOT_KEEP_ALIVE, BOT_KEEP_ALIVE)
    }

    override fun run() {
        if (!alive.get())
            timer.cancel()
    }

}
