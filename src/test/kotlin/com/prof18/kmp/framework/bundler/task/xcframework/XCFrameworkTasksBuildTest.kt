package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.data.FrameworkType
import com.prof18.kmp.framework.bundler.testutils.BaseBuildTaskTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class XCFrameworkTasksBuildTest: BaseBuildTaskTest(frameworkType = FrameworkType.XC_FRAMEWORK) {

    @Before
    override fun setup() {
//        System.setProperty("kotlinVersion", "1.5.0");

        println("version: ${System.getProperty("kotlinVersion")}")

        super.setup()

    }

    @Test
    fun `When running the build debug xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        runner
            .withArguments(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME)
            .build()

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }

    @Test
    fun `When running the build release xcFramework task, the xcFramework is placed inside the correct destination folder`() {
        runner
            .withArguments(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME)
            .build()

        assertTrue(testDestFile.listFiles()?.any { it.name == "LibraryName.xcframework" } ?: false)
    }
}
