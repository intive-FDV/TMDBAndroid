package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val dao: Dao
) {

    suspend fun existSession(): Flow<List<Session>> {
        return flowOf(dao.existSession().map {
            it.toSession()
        })
    }

    suspend fun insert(session: Session) {
        dao.insertSession(session.toSessionORMEntity())
    }
}