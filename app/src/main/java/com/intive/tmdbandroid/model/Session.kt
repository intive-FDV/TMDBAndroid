package com.intive.tmdbandroid.model

class Session {
    var Success = false

    lateinit var sessionId:String
    private var accountId = 0

    fun Session(success: Boolean, sessionId: String) {
        Success = success
        this.sessionId = sessionId
    }

    fun getAccountId(): Int {
        return accountId
    }

    fun setAccountId(accountId: Int) {
        this.accountId = accountId
    }
}