package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.data.FrameworkType
import com.prof18.kmp.framework.bundler.testutils.BaseBuildTaskTest
import com.prof18.kmp.framework.bundler.testutils.buildAndRun
import junit.framework.TestCase.assertTrue
import org.junit.Test

internal class FatFrameworkTasksBuildTest : BaseBuildTaskTest(frameworkType = FrameworkType.FAT_FRAMEWORK) {

    @Test
    fun `When running the build debug fat framework task, the fat framework and debug symbols are placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.framework" } ?: false)
        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.framework.dSYM" } ?: false)
    }

    @Test
    fun `When running the build release fat framework task, the fat framework and debug symbols are placed inside the correct destination folder`() {
        testProject.buildAndRun(BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME)

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.framework" } ?: false)
        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.framework.dSYM" } ?: false)
    }
}
