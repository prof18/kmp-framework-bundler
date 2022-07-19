package com.prof18.kmpframeworkbundler.task.xcframework

import com.prof18.kmpframeworkbundler.data.FrameworkType
import com.prof18.kmpframeworkbundler.testutils.BaseBuildTaskTest
import com.prof18.kmpframeworkbundler.testutils.buildAndRun
import com.prof18.kmpframeworkbundler.testutils.extractKotlinVersion
import com.prof18.kmpframeworkbundler.testutils.getCurrentKotlinVersion
import com.prof18.kmpframeworkbundler.testutils.getDefaultKotlinVersion
import com.prof18.kmpframeworkbundler.testutils.hasDefaultXCFrameworkSupport
import com.prof18.kmpframeworkbundler.testutils.resetKotlinVersionToDefault
import com.prof18.kmpframeworkbundler.testutils.setKotlinVersion
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class XCFrameworkTasksBuildTest(
    private val kotlinVersion: String,
) : BaseBuildTaskTest(frameworkType = FrameworkType.XC_FRAMEWORK) {

    @Before
    override fun setup() {
        setKotlinVersion(kotlinVersion)

        val rawKotlinVersion = getCurrentKotlinVersion()
        extractKotlinVersion(rawKotlinVersion)?.let { kotlinVersion ->
            if (!kotlinVersion.hasDefaultXCFrameworkSupport()) {
                hasToDeleteImport = true
            }
        }

        super.setup()
    }

    @After
    override fun cleanUp() {
        resetKotlinVersionToDefault()
        super.cleanUp()
    }

    @Test
    fun `When running the build debug xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }

    @Test
    fun `When running the build release xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "with Kotlin {0}")
        fun data() = listOf(
            arrayOf("1.5.30"),
            arrayOf(getDefaultKotlinVersion()),
        )
    }
}
