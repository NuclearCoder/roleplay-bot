package org.kud.roleplay.database.adapter

import org.kud.roleplay.DATABASE_DRIVER
import org.kud.roleplay.LOGGER
import org.kud.roleplay.util.Config
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

abstract class DatabaseAdapter(private val config: Config) {

    protected var connection: Connection? = null

    init {
        try {
            Class.forName(DATABASE_DRIVER).newInstance()
        } catch (e: InstantiationException) {
            LOGGER.error("Could not load database driver.", e)
        } catch (e: IllegalAccessException) {
            LOGGER.error("Could not load database driver.", e)
        } catch (e: ClassNotFoundException) {
            LOGGER.error("Could not load database driver.", e)
        }
    }

    fun connect() {
        val connectionProps = Properties()
        connectionProps["user"] = config["database_user"]
        connectionProps["password"] = config["database_pass"]
        connectionProps["useSSL"] = "false"
        connectionProps["autoReconnect"] = "true"

        val host = config["database_host"]
        val port = config["database_port"]
        val schema = config["database_schema"]

        try {
            connection = DriverManager.getConnection("jdbc:mysql://$host:$port/$schema", connectionProps)
            LOGGER.info("Connected to database.")
        } catch (e: SQLException) {
            LOGGER.error("Could not open database connection.", e)
        }
    }

    fun disconnect() {
        if (connection != null) {
            connection?.close()
            LOGGER.info("Disconnected from database.")
        }
    }

}