package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.task.fatframework.BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.task.fatframework.BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.task.fatframework.PublishDebugFatFrameworkTask
import com.prof18.kmp.framework.bundler.task.fatframework.PublishReleaseFatFrameworkTask
import com.prof18.kmp.framework.bundler.testutils.baseXCFrameworkGradleFile
import com.prof18.kmp.framework.bundler.testutils.buildAndFail
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

@RunWith(Parameterized::class)
class XCFrameworkNotRegisteredTasksTest(
    private val taskName: String,
) {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var tempBuildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder

    @Before
    fun setup() {
        val testProjectName = "test-project"
        testProject = File("src/test/resources/$testProjectName")
        buildGradleFile = File("src/test/resources/$testProjectName/build.gradle.kts")
        tempBuildGradleFile = File("src/test/resources/test-project/build.gradle.kts.new")
        buildGradleFile.copyTo(tempBuildGradleFile)

        gradleFileStringBuilder = StringBuilder()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        tempBuildGradleFile.renameTo(buildGradleFile)
        File("${testProject.path}/build").deleteRecursively()
    }

    @Test
    fun `When XC_FRAMEWORK is setup, the fat framework tasks are not present`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseXCFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail(taskName)

        assertTrue(result.output.contains("Task '$taskName' not found in root project"))
    }

    @Test
    fun `When XC_FRAMEWORK_LEGACY_BUILD is setup, the fat framework tasks are not present`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseXCFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail(taskName)
        assertTrue(result.output.contains("Task '$taskName' not found in root project"))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(BUILD_DEBUG_FAT_FRAMEWORK_TASK_NAME),
            arrayOf(BUILD_RELEASE_FAT_FRAMEWORK_TASK_NAME),
            arrayOf(PublishDebugFatFrameworkTask.NAME),
            arrayOf(PublishReleaseFatFrameworkTask.NAME),
        )
    }
}