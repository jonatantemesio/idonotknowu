package com.idonotknowu.callblocker.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BlockedCallDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: BlockedCallDao

    @Before fun open() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(), AppDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = db.blockedCallDao()
    }

    @After fun close() = db.close()

    @Test fun emptyByDefault() = runBlocking {
        assertTrue(dao.observeAll().first().isEmpty())
    }

    @Test fun ordersNewestFirstAndAssignsIds() = runBlocking {
        dao.insert(BlockedCall(number = "111", timestamp = 1_000))
        dao.insert(BlockedCall(number = "333", timestamp = 3_000))
        dao.insert(BlockedCall(number = "222", timestamp = 2_000))

        val all = dao.observeAll().first()
        assertEquals(listOf("333", "222", "111"), all.map { it.number })
        assertEquals(3, all.map { it.id }.toSet().size)
    }

    @Test fun keepsRepeatedCallsFromSameNumber() = runBlocking {
        dao.insert(BlockedCall(number = "Desconhecido", timestamp = 1))
        dao.insert(BlockedCall(number = "Desconhecido", timestamp = 2))
        assertEquals(2, dao.observeAll().first().size)
    }
}
