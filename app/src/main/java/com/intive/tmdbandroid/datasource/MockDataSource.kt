package com.intive.tmdbandroid.datasource

import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.entity.ResultMovies
import com.intive.tmdbandroid.entity.ResultTVShowsEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockDataSource {
    fun mokkPopularMovies(): Flow<ResultMovies> {
        return flow {
            delay(3000)

            val movie1 = Movie(true,"", listOf(),0,"en-US","Pulp Fiction",""
                ,100.00,"/hPeKa7IFm44zvMVM4njcv66clj3.jpg","02/16/1995","",true
                ,0.0,0)
            val movie2 = Movie(true,"", listOf(),0,"en-US","Reservoir Dogs",""
                ,100.00,"/AjTtJNumZyUDz33VtMlF1K8JPsE.jpg","09/01/1994","",true
                ,0.0,0)

            emit(ResultMovies(1, arrayListOf(movie1,movie2,movie1,movie2,movie1,movie2),1,2))
        }
    }

    fun mokkPopularTVShows(): Flow<ResultTVShowsEntity> {
        return flow {
            delay(3000)

            emit(ResultTVShowsEntity(1, arrayListOf(),1,2))
        }
    }
}