package com.intive.tmdbandroid.datasource.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.Genre
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class LocalStorageTest : TestCase() {
    // get reference to the Database and Dao class
    private lateinit var db: LocalStorage
    private lateinit var dao: Dao

    // Override function setUp() and annotate it with @Before
    // this function will be called at first when this test class is called
    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, LocalStorage::class.java).build()
        dao = db.tvShowDao()
    }

    // Override function closeDb() and annotate it with @After
    // this function will be called at last when this test class is called
    @After
    fun closeDb() {
        db.close()
    }

    // create a test function and annotate it with @Test
    // here we are first adding an item to the db and then checking if that item
    // is present in the db -- if the item is present then our test cases pass
    @Test
    fun writeAndReadTvShow() = runBlocking {
        val tvShow = TVShowORMEntity(
            backdrop_path = "BACKDROP_PATH",
            first_air_date = "1983-10-20",
            genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
            id = 1,
            name = "Simona la Cacarisa",
            original_name = "El cochiloco",
            overview = "Simona la cacarisa, el cochiloco",
            poster_path = "POSTER_PATH",
            vote_average = 10.5,
            vote_count = 100,
            created_by = emptyList(),
            last_air_date = "1990-09-25",
            number_of_episodes = 5,
            number_of_seasons = 2,
            status = "Online"
        )
        dao.insertFavorite(tvShow)
        val tvShows = dao.allFavorite()
        Assert.assertEquals(tvShows[0], tvShow)
    }
}