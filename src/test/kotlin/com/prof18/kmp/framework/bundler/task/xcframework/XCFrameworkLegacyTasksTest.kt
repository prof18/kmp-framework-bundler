package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.data.FrameworkType
import com.prof18.kmp.framework.bundler.testutils.BaseTaskTest
import junit.framework.TestCase
import org.junit.Test

class XCFrameworkLegacyTasksTest : BaseTaskTest(frameworkType = FrameworkType.XC_FRAMEWORK_LEGACY_BUILD) {

    @Test
    fun `When running the legacy build debug xcFramework task, the xcFramework is placed inside the correct destination folder`() {
       runner
            .withArguments(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME)
            .build()

        TestCase.assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.xcframework" })
    }

    @Test
    fun `When running the legacy build release xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        runner
            .withArguments(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME)
            .build()

        TestCase.assertTrue(testDestFile.listFiles().any { it.name == "LibraryName.xcframework" })
    }
}
