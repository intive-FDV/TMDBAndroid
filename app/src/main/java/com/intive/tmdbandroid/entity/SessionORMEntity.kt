package com.intive.tmdbandroid.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.intive.tmdbandroid.model.Session

@Entity
data class SessionORMEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val success:Boolean,
    val guest_session_id: String,
    val expires_at: String,
    val status_message: String,
    val status_code: Int
) {
    fun toSession(): Session {
        return Session(
            id,
            success,
            guest_session_id,
            expires_at,
            status_message,
            status_code
        )
    }
}
