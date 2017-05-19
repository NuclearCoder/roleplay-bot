package org.kud.roleplay.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.util.Config

class Database(config: Config) {

    private val host = config["database_host"]
    private val port = config["database_port"]
    private val schema = config["database_schema"]

    private val user = config["database_user"]
    private val pass = config["database_pass"]

    init {
        Database.connect("jdbc:mysql://$host:$port/$schema", driver = "com.mysql.jdbc.Driver",
                user = user, password = pass)

        transaction {
            create(Guilds, Users, Characters)
        }
    }

}