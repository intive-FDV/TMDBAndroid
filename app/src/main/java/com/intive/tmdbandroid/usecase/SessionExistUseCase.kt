package com.intive.tmdbandroid.usecase

import com.intive.tmdbandroid.model.Session
import com.intive.tmdbandroid.repository.SessionRepository
import javax.inject.Inject

class SessionExistUseCase @Inject constructor(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(): Session = sessionRepository.existSession()
}