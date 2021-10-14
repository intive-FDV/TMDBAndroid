package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(session: Session): Flow<Boolean> = sessionRepository.insert(session)
}