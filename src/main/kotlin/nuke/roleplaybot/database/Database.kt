package nuke.roleplaybot.database

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import nuke.roleplaybot.util.Config
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object Database {

    fun load(config: Config) {
        Database.connect(MysqlDataSource().apply {
            serverName = config["database_host"]
            port = config["database_port"].toInt()
            useSSL = false

            databaseName = config["database_schema"]

            user = config["database_user"]
            setPassword(config["database_pass"])
        })

        transaction {
            create (Items) // that first
            create (Guilds, Users, Characters)
        }
    }

}