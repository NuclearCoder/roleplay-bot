package org.kud.roleplay.util

import org.kud.roleplay.CONFIG_AUTOSAVE_PERIOD
import org.kud.roleplay.CONFIG_FILENAME
import org.kud.roleplay.LOGGER
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class Config {

    private val file = File(CONFIG_FILENAME)
    private val properties = Properties()

    private val config = mutableMapOf<String, String>()

    private val hasChanged = AtomicBoolean(false)

    init {
        // create file if it doesn't exist
        if (!file.exists()) {
            LOGGER.warn("Config file does not exist, creating...")
            try {
                if (!(file.mkdirs() && file.delete() && file.createNewFile())) {
                    LOGGER.error("Could not create file.")
                }
            } catch (e: IOException) {
                LOGGER.error("Could not create file.", e)
            }
        }

        // then load first time
        load()

        // finally, start autosaving
        object : TimerTask() {
            override fun run() {
                if (hasChanged.compareAndSet(true, false)) {
                    save()
                }
            }
        }.also { Timer("config-autosave", true).schedule(it, CONFIG_AUTOSAVE_PERIOD, CONFIG_AUTOSAVE_PERIOD) }
    }

    fun load() {
        try {
            FileReader(file).use { properties.load(it) }
        } catch (e: IOException) {
            LOGGER.error("Could not load config.", e)
        }
    }

    fun save() {
        try {
            FileWriter(file).use { properties.store(it, "please do not edit this manually") }
        } catch (e: IOException) {
            LOGGER.error("Could not load config.", e)
        }
    }

    operator fun get(key: String): String {
        val value = config[key]
        if (value != null) return value
        else {
            config[key] = ""
            return ""
        }
    }

    operator fun set(key: String, value: Any?) {
        config[key] = value.toString()
        // we've changed config, update the changed flag
        hasChanged.set(true)
    }

}