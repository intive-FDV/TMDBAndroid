package com.intive.tmdbandroid.model

import com.intive.tmdbandroid.entity.SessionORMEntity

data class Session(
    val id: Int,
    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String,
    var status_message: String,
    var status_code: Int

) {
    fun toSessionORMEntity(): SessionORMEntity {
        return SessionORMEntity(
            id,
            success,
            guest_session_id,
            expires_at,
            status_message,
            status_code,

            )
    }
}
