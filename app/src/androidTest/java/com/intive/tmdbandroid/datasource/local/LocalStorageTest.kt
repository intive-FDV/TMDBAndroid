package com.intive.tmdbandroid.datasource.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.intive.tmdbandroid.entity.ScreeningORMEntity
import com.intive.tmdbandroid.model.Genre
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class) // Annotate with @RunWith
class LocalStorageTest : TestCase() {
    // get reference to the Database and Dao class
    private lateinit var db: LocalStorage
    private lateinit var dao: Dao

    private val screening = ScreeningORMEntity(
        backdrop_path = "BACKDROP_PATH",
        release_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
        id = 1,
        name = "Simona la Cacarisa",
        number_of_episodes = 5,
        number_of_seasons = 2,
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH",
        status = "Online",
        vote_average = 10.5,
        vote_count = 100,
        popularity = 34.0,
        media_type = "tv",
        adult = false,
        genre_ids = null,
        video = false
    )

    private val screeningUpdate = ScreeningORMEntity(
        backdrop_path = "BACKDROP_PATH_2",
        release_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
        id = 1,
        name = "Simona la Cacarisa",
        number_of_episodes = 5,
        number_of_seasons = 2,
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH_2",
        status = "Online",
        vote_average = 10.5,
        vote_count = 100,
        popularity = 34.0,
        media_type = "tv",
        adult = false,
        genre_ids = null,
        video = false
    )

    // Override function setUp() and annotate it with @Before
    // this function will be called at first when this test class is called
    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        val context = ApplicationProvider.getApplicationContext<Context>()
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, LocalStorage::class.java).build()
        dao = db.screeningDao()
    }

    // Override function closeDb() and annotate it with @After
    // this function will be called at last when this test class is called
    @After
    fun closeDb() {
        db.close()
    }

    // here we are first adding an item to the db and then checking if that item
    // is present in the db -- if the item is present then we try to delete it and
    // check the db once more -- if the item is not present, our test case passes
    @Test
    fun writeAndReadAllAndDeleteTest() = runBlocking {
        dao.insertFavorite(screening)
        val screenings = dao.allFavorites()

        assertThat("Expecting to find item in list", screenings.contains(screening))

        dao.deleteFavorite(screening)
        val screeningDeleted = dao.allFavorites()

        assertThat("Expecting NOT to find item in list", !screeningDeleted.contains(screening))
    }

    // here we are first adding an item to the db and then checking if that item
    // is present in the db -- if the item is present then we try to update it and
    // check the db once more -- if the old item is not present and the new one is,
    // our test case passes
    @Test
    fun writeAndReadOneSuccessAndUpdateTest() = runBlocking {
        dao.insertFavorite(screening)
        val exists = dao.existAsFavorite(screening.id)

        assertThat("Expecting result to be true", exists)

        dao.updateFavorite(screeningUpdate)
        val tvShowsDeleted = dao.allFavorites()

        assertThat("Expecting NOT to find old item in list", !tvShowsDeleted.contains(screening))
        assertThat("Expecting to find new item in list", tvShowsDeleted.contains(screeningUpdate))
    }

    // here we are first checking if an item is present in the db  without adding it
    // -- if the item is NOT present, our test case passes
    @Test
    fun writeAndReadOneFailureTest() = runBlocking {
        val exists = dao.existAsFavorite(screening.id)

        assertThat("Expecting result to be true", !exists)
    }
}
