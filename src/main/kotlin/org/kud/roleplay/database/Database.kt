package org.kud.roleplay.database

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import org.kud.roleplay.util.Config


class Database(config: Config) {

    init {
        Database.connect(MysqlDataSource().apply {
            serverName = config["database_host"]
            port = config["database_port"].toInt()
            useSSL = false

            databaseName = config["database_schema"]

            user = config["database_user"]
            setPassword(config["database_pass"])
        })

        transaction {
            create(Guilds, Users, Characters)
        }
    }

}