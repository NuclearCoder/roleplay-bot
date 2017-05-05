package org.kud.roleplay.database

import org.kud.roleplay.database.adapter.DatabaseAdapter
import org.kud.roleplay.database.adapter.request.CachedRequest
import org.kud.roleplay.roleplay.Character
import org.kud.roleplay.util.Config
import org.kud.roleplay.util.use
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*

class Database(config: Config) : DatabaseAdapter(config) {

    private val timer = Timer("database cache sweep", true)

    private val reqRoleplayRoleForGuild = CachedRequest(timer, "SELECT rp_role FROM guilds WHERE id = ?;")
    { st, guildId: Long ->
        st.setLong(1, guildId)
        st.executeQuery().let {
            if (it.first()) it.getLong("rp_role")
            else -1
        }
    }

    private val reqRoleplayCharacterContentList = CachedRequest(timer, "SELECT name, content FROM characters WHERE (id_guild, id_user) = (?, ?);")
    { st, (guildId, userId): Pair<Long, Long> ->
        st.setLong(1, guildId)
        st.setLong(2, userId)
        st.executeQuery().let {
            val list = mutableListOf<Character>()
            while (it.next()) {
                list.add(Character(guildId, userId, it.getString("name"), it.getString("content")))
            }
            list
        }
    }

    private val reqRoleplayCharacterContent = CachedRequest(timer, "SELECT content FROM characters WHERE (id_guild, id_user, name) = (?, ?, ?);")
    { st, (guildId, userId, name): Triple<Long, Long, String> ->
        st.setLong(1, guildId)
        st.setLong(2, userId)
        st.setString(3, name)
        st.executeQuery().let {
            if (it.first()) Character(guildId, userId, name, it.getString("content"))
            else null
        }
    }

    private val reqRoleplayCharacterExists = CachedRequest(timer, "SELECT NULL FROM characters WHERE (id_guild, id_user, name) = (?, ?, ?);")
    { st, (guildId, userId, name): Triple<Long, Long, String> ->
        st.setLong(1, guildId)
        st.setLong(2, userId)
        st.setString(3, name)
        st.executeQuery().first()
    }

    fun getRoleplayRoleForGuild(guildId: Long) =
            reqRoleplayRoleForGuild.get(connection!!, guildId)

    fun getRoleplayCharacterList(guildId: Long, userId: Long): List<Character> =
            reqRoleplayCharacterContentList.get(connection!!, Pair(guildId, userId))

    fun getRoleplayCharacter(guildId: Long, userId: Long, name: String) =
            reqRoleplayCharacterContent.get(connection!!, Triple(guildId, userId, name))

    fun existsRoleplayCharacter(guildId: Long, userId: Long, name: String) =
            reqRoleplayCharacterExists.get(connection!!, Triple(guildId, userId, name))

    fun createRoleplayCharacter(guildId: Long, userId: Long, name: String) {
        query(connection!!, "INSERT INTO characters (id_guild, id_user, name, content) VALUES (?, ?, ?, '');") { st ->
            st.setLong(1, guildId)
            st.setLong(2, userId)
            st.setString(3, name)
            st.executeUpdate()
            // update cache
            reqRoleplayCharacterExists.clearCache()
        }
    }

    fun deleteRoleplayCharacter(guildId: Long, userId: Long, name: String) {
        query(connection!!, "DELETE FROM characters WHERE (id_guild, id_user, name) = (?, ?, ?);") { st ->
            st.setLong(1, guildId)
            st.setLong(2, userId)
            st.setString(3, name)
            st.executeUpdate()
            // update cache
            reqRoleplayCharacterExists.clearCache()
        }
    }

    fun updateRoleplayCharacter(guildId: Long, userId: Long, name: String, content: String) {
        query(connection!!, "UPDATE characters SET content = ? WHERE (id_guild, id_user, name) = (?, ?, ?);") { st ->
            st.setString(1, content)
            st.setLong(2, guildId)
            st.setLong(3, userId)
            st.setString(4, name)
            st.executeUpdate()
        }
    }

    /* - methods - */

    fun clearCache() {
        reqRoleplayRoleForGuild.clearCache()
        reqRoleplayCharacterContentList.clearCache()
        reqRoleplayCharacterContent.clearCache()
        reqRoleplayCharacterExists.clearCache()
    }

    private inline fun query(conn: Connection, query: String, block: (PreparedStatement) -> Unit) =
            conn.prepareStatement(query).use(block)

}