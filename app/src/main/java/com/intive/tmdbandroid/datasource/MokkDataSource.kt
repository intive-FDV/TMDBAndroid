package com.intive.tmdbandroid.datasource

import android.util.Log
import com.intive.tmdbandroid.model.Movie
import com.intive.tmdbandroid.model.ResultMovies
import com.intive.tmdbandroid.model.ResultTVShows
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MokkDataSource {
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

    fun mokkPopularTVShows(): Flow<ResultTVShows> {
        return flow {
            delay(3000)

            val tvShow1 = TVShow("","22-09-1994", listOf(),0,"", listOf(),"en-US"
                ,"Friends","",100.00,"/1uIMhXNXpQTVEdLSpKWjbsCzIEc.jpg",0.0,0)
            val tvShow2 = TVShow("","19-09-2005", listOf(),0,"", listOf(),"en-US"
                ,"How I met your mother","",100.00,"/b34jPzmB0wZy7EjUZoleXOl2RRI.jpg",0.0,0)

            emit(ResultTVShows(1, arrayListOf(tvShow1, tvShow2,tvShow1, tvShow2,tvShow1, tvShow2),1,2))
        }
    }
}