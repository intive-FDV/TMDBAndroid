package com.intive.tmdbandroid.repository

import com.intive.tmdbandroid.datasource.local.Dao
import com.intive.tmdbandroid.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val dao: Dao
) {

    suspend fun existSession(): Session {
        val myFlow = flowOf(dao.existSession())
        var returnSession = Session(0,false,"","","",0)
        if(myFlow.single().size>0){
            returnSession = myFlow.single().last().toSession()
        }
        return returnSession
    }

    suspend fun insert(session: Session): Flow<Boolean> {
        dao.insertSession(session.toSessionORMEntity())
        return flowOf(true)
    }
}