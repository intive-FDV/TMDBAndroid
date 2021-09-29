package com.intive.tmdbandroid.model

data class Session (
     val success:Boolean,
      val guest_session_id:String,
      val expires_at:String,
)