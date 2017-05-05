package org.kud.roleplay.database.adapter.request

import java.sql.Connection

@FunctionalInterface interface Request<in D, out T> {

    fun get(conn: Connection, data: D): T

}