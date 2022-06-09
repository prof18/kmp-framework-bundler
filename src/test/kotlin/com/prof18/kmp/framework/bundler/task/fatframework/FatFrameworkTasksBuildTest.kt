package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.data.FrameworkType
import com.prof18.kmp.framework.bundler.testutils.BaseBuildTaskTest
import junit.framework.TestCase.assertTrue
import org.junit.Test

internal class FatFrameworkTasksBuildTest : BaseBuildTaskTest(frameworkType = FrameworkType.FAT_FRAMEWORK) {

    @Test
    fun `When running the build debug fat framework task, the fat framework and debug symbols are placed inside the correct destination folder`() {
        runner
            .withArguments(BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME)
            .build()

        assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.framework" })
        assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.framework.dSYM" })
    }

    @Test
    fun `When running the build release fat framework task, the fat framework and debug symbols are placed inside the correct destination folder`() {
        runner
            .withArguments(BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME)
            .build()

        assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.framework" })
        assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.framework.dSYM" })
    }


}