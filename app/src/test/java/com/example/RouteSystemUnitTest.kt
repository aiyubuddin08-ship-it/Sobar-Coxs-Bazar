package com.example

import com.example.data.datasource.RouteData
import com.example.data.model.RouteStepItem
import com.example.data.network.OsrmRoutingService
import org.junit.Assert.*
import org.junit.Test

class RouteSystemUnitTest {

    @Test
    fun testDistanceFormattingBangla() {
        val shortDist = OsrmRoutingService.formatDistanceBn(450.0)
        assertEquals("৪৫০ মিটার", shortDist)

        val longDist = OsrmRoutingService.formatDistanceBn(12500.0)
        assertEquals("১২.৫ কিমি", longDist)

        val zeroDist = OsrmRoutingService.formatDistanceBn(0.0)
        assertEquals("০ মিটার", zeroDist)
    }

    @Test
    fun testDurationFormattingBangla() {
        val underMinute = OsrmRoutingService.formatDurationBn(35.0)
        assertEquals("১ মিনিটের কম", underMinute)

        val minutesOnly = OsrmRoutingService.formatDurationBn(1500.0) // 25 mins
        assertEquals("২৫ মিনিট", minutesOnly)

        val hoursAndMinutes = OsrmRoutingService.formatDurationBn(4500.0) // 75 mins = 1h 15m
        assertEquals("১ ঘণ্টা ১৫ মিনিট", hoursAndMinutes)

        val exactHour = OsrmRoutingService.formatDurationBn(7200.0) // 120 mins = 2h
        assertEquals("২ ঘণ্টা", exactHour)
    }

    @Test
    fun testMaheshkhaliRequiresFerryAndHasVerifiedGhat() {
        val maheshkhali = RouteData.initialDestinations.find { it.id == "dest_maheshkhali" }
        assertNotNull("Maheshkhali destination must be present", maheshkhali)
        assertTrue("Maheshkhali is an island and must require ferry", maheshkhali!!.requiresFerry)
        assertNotNull("Ferry ghat name must be specified", maheshkhali.ferryGhatNameBn)
        assertTrue("Ghat must be Kasturi / 6 No / Naniarchara", maheshkhali.ferryGhatNameBn!!.contains("কস্তুরী ঘাট"))
        assertNotNull("Ferry duration and fare guidance must be present", maheshkhali.ferryDurationBn)
    }

    @Test
    fun testNaniarcharaDestinationPresent() {
        val naniarchara = RouteData.initialDestinations.find { it.id == "dest_naniarchara" }
        assertNotNull("Naniarchara destination must be present", naniarchara)
        assertEquals("নানিয়ারছড়া ঘাট ও সংযোগ পয়েন্ট", naniarchara!!.nameBn)
        assertTrue("Latitude must be valid Cox's Bazar region", naniarchara.latitude in 21.0..22.0)
        assertTrue("Longitude must be valid Cox's Bazar region", naniarchara.longitude in 91.0..93.0)
    }

    @Test
    fun testCoreDestinationsCoordinatesValidity() {
        val destinations = RouteData.initialDestinations
        assertTrue("Destinations catalog should have at least 15 verified points", destinations.size >= 15)

        for (dest in destinations) {
            assertTrue("Latitude for ${dest.nameBn} must be in valid range", dest.latitude in -90.0..90.0)
            assertTrue("Longitude for ${dest.nameBn} must be in valid range", dest.longitude in -180.0..180.0)
            assertTrue("Category must not be blank", dest.category.isNotBlank())
            assertEquals("Default status should be verified", "যাচাইকৃত (Verified)", dest.verificationStatus)
        }
    }

    @Test
    fun testVerifiedStartingPointsList() {
        val startingPoints = RouteData.verifiedStartingPoints
        assertTrue("Starting points list must not be empty", startingPoints.isNotEmpty())
        val laboni = startingPoints.find { it.nameBn.contains("লাবণী") }
        assertNotNull("Laboni point should be in starting points", laboni)
    }

    @Test
    fun testSimplifyRouteStepsMergesRepetitiveStraightsWhilePreservingTurns() {
        val rawSteps = listOf(
            RouteStepItem(
                instructionBn = "যাত্রা শুরু করুন (কলাতলী মোড়)",
                streetName = "কলাতলী রোড",
                distanceTextBn = "৫০ মিটার",
                durationTextBn = "১ মিনিটের কম",
                maneuverType = "depart",
                distanceMeters = 50.0,
                durationSeconds = 10.0
            ),
            RouteStepItem(
                instructionBn = "সোজা রাস্তা ধরে এগিয়ে যান (মেরিন ড্রাইভ)",
                streetName = "মেরিন ড্রাইভ",
                distanceTextBn = "২০০ মিটার",
                durationTextBn = "১ মিনিটের কম",
                maneuverType = "continue",
                modifier = "straight",
                distanceMeters = 200.0,
                durationSeconds = 30.0
            ),
            RouteStepItem(
                instructionBn = "সোজা রাস্তা ধরে এগিয়ে যান (মেরিন ড্রাইভ)",
                streetName = "মেরিন ড্রাইভ",
                distanceTextBn = "৩০০ মিটার",
                durationTextBn = "১ মিনিটের কম",
                maneuverType = "continue",
                modifier = "straight",
                distanceMeters = 300.0,
                durationSeconds = 40.0
            ),
            RouteStepItem(
                instructionBn = "সোজা রাস্তা ধরে এগিয়ে যান (মেরিন ড্রাইভ)",
                streetName = "মেরিন ড্রাইভ",
                distanceTextBn = "৫০০ মিটার",
                durationTextBn = "১ মিনিট",
                maneuverType = "continue",
                modifier = "straight",
                distanceMeters = 500.0,
                durationSeconds = 60.0
            ),
            RouteStepItem(
                instructionBn = "ডানে মোড় নিন (ইনানী বিচ রোড)",
                streetName = "ইনানী বিচ রোড",
                distanceTextBn = "৮০ মিটার",
                durationTextBn = "১ মিনিটের কম",
                maneuverType = "turn",
                modifier = "right",
                distanceMeters = 80.0,
                durationSeconds = 15.0
            ),
            RouteStepItem(
                instructionBn = "আপনার গন্তব্যে পৌঁছে গেছেন",
                streetName = "ইনানী বিচ",
                distanceTextBn = "০ মিটার",
                durationTextBn = "০ মিনিট",
                maneuverType = "arrive",
                distanceMeters = 0.0,
                durationSeconds = 0.0
            )
        )

        val simplified = OsrmRoutingService.simplifyRouteSteps(rawSteps)

        assertEquals(4, simplified.size)
        assertEquals("depart", simplified[0].maneuverType)
        assertEquals("continue", simplified[1].maneuverType)
        assertEquals(1000.0, simplified[1].distanceMeters, 0.1)
        assertEquals("১.০ কিমি", simplified[1].distanceTextBn)
        assertEquals("turn", simplified[2].maneuverType)
        assertEquals("right", simplified[2].modifier)
        assertEquals("arrive", simplified[3].maneuverType)
    }

    @Test
    fun testSimplifyRouteStepsPreservesShortList() {
        val shortList = listOf(
            RouteStepItem(
                instructionBn = "যাত্রা শুরু করুন",
                streetName = "রোড ১",
                distanceTextBn = "১০০ মিটার",
                durationTextBn = "১ মিনিটের কম",
                maneuverType = "depart"
            ),
            RouteStepItem(
                instructionBn = "আপনার গন্তব্যে পৌঁছে গেছেন",
                streetName = "হোটেল সিগাল",
                distanceTextBn = "০ মিটার",
                durationTextBn = "০ মিনিট",
                maneuverType = "arrive"
            )
        )
        val result = OsrmRoutingService.simplifyRouteSteps(shortList)
        assertEquals(2, result.size)
    }
}
