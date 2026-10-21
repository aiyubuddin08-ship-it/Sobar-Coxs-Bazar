package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.CoxRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("কক্সবাজার", appName)
    }

    @Test
    fun `repository loads cox's bazar demo data`() {
        val repository = CoxRepository()
        assertEquals(10, repository.spots.value.size)
        assertEquals(12, repository.hotels.value.size)
        assertEquals(10, repository.restaurants.value.size)
        assertEquals(8, repository.shops.value.size)
        assertEquals(8, repository.transports.value.size)
        assertEquals(8, repository.emergencies.value.size)
        assertEquals(10, repository.news.value.size)
        assertEquals(8, repository.events.value.size)
        assertTrue(repository.favoriteIds.value.isNotEmpty())
    }
}
