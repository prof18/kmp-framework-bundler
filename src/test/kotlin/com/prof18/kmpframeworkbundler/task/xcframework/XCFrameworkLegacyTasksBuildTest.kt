package com.prof18.kmpframeworkbundler.task.xcframework

import com.prof18.kmpframeworkbundler.data.FrameworkType
import com.prof18.kmpframeworkbundler.testutils.BaseBuildTaskTest
import com.prof18.kmpframeworkbundler.testutils.buildAndRun
import junit.framework.TestCase.assertTrue
import org.junit.Test

class XCFrameworkLegacyTasksBuildTest : BaseBuildTaskTest(frameworkType = FrameworkType.XC_FRAMEWORK_LEGACY_BUILD) {
    @Test
    fun `When running the legacy build debug xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }

    @Test
    fun `When running the legacy build release xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }
}
