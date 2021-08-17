package com.intive.tmdbandroid.sample.entity

import com.intive.tmdbandroid.sample.domain.Sample

data class SampleEntity(val dummy: String) {
    fun toSample(): Sample {
        return Sample(dummy = dummy)
    }
}
