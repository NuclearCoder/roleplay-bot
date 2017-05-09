package org.kud.roleplay.database.adapter.request

import org.kud.roleplay.DATABASE_CACHE_SWEEP_PERIOD
import org.kud.roleplay.DATABASE_CACHE_THRESHOLD
import org.kud.roleplay.DATABASE_CACHE_TIMEOUT
import org.kud.roleplay.util.use
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.schedule

class CachedRequest<in D, out R>(timer: Timer, private val request: Request<D, R>) : Request<D, R> {

    constructor(timer: Timer, query: String, block: (PreparedStatement, D) -> R)
            : this(timer, object : Request<D, R> {
        override fun get(conn: Connection, data: D) =
                conn.prepareStatement(query).use { block(it, data) }
    })

    init {
        timer.schedule(DATABASE_CACHE_SWEEP_PERIOD, DATABASE_CACHE_SWEEP_PERIOD) {
            val now = System.currentTimeMillis()
            cache.forEachEntry(DATABASE_CACHE_THRESHOLD) {
                if (now - it.value.timeCreated > DATABASE_CACHE_TIMEOUT) {
                    cache.remove(it.key)
                }
            }
        }
    }

    // elapsed is lifetime in milliseconds
    private data class CacheResult<out T>(val x: T, val timeCreated: Long)

    private val cache = ConcurrentHashMap<D, CacheResult<R>>()

    fun clearCache() = cache.clear()

    override fun get(conn: Connection, data: D): R =
            cache[data]?.x ?: request.get(conn, data).also {
                cache[data] = CacheResult(it, System.currentTimeMillis())
            }

}
