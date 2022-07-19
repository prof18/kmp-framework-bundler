package com.prof18.kmpframeworkbundler.testutils

import org.junit.Assert.*
import org.junit.Test

class ExtractKotlinVersionTest {

    @Test
    fun `extractKotlinVersion works correctly`() {
        val versionString = "1.6.20"

        val expectedVersion = KotlinVersion(1, 6, 20)
        val version = extractKotlinVersion(versionString)
        assertEquals(expectedVersion, version)
    }

    @Test
    fun `extractKotlinVersion returns null if a version part is null`() {
        val versionString = "1.6"

        val version = extractKotlinVersion(versionString)
        assertNull(version)
    }

    @Test
    fun `extractKotlinVersion works correctly with eap`() {
        val versionString = "1.3.70-eap-274"

        val expectedVersion = KotlinVersion(1, 3, 70)
        val version = extractKotlinVersion(versionString)
        assertEquals(expectedVersion, version)
    }

    @Test
    fun `extractKotlinVersion works correctly with RC`() {
        val versionString = "1.6.20-EAP"

        val expectedVersion = KotlinVersion(1, 6, 20)
        val version = extractKotlinVersion(versionString)
        assertEquals(expectedVersion, version)
    }

}