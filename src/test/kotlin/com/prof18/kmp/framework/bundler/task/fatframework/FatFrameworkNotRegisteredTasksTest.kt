package com.prof18.kmp.framework.bundler.task.fatframework

import com.prof18.kmp.framework.bundler.task.xcframework.BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.task.xcframework.BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.task.xcframework.PublishDebugXCFrameworkTask
import com.prof18.kmp.framework.bundler.task.xcframework.PublishReleaseXCFrameworkTask
import com.prof18.kmp.framework.bundler.testutils.baseFatFrameworkGradleFile
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

@RunWith(Parameterized::class)
class FatFrameworkNotRegisteredTasksTest(
    private val taskName: String,
) {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var tempBuildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder
    private lateinit var runner: GradleRunner

    @Before
    fun setup() {
        val testProjectName = "test-project"
        testProject = File("src/test/resources/$testProjectName")
        buildGradleFile = File("src/test/resources/$testProjectName/build.gradle.kts")
        tempBuildGradleFile = File("src/test/resources/$testProjectName/build.gradle.kts.new")
        buildGradleFile.copyTo(tempBuildGradleFile)

        gradleFileStringBuilder = StringBuilder()

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.FAT_FRAMEWORK
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        tempBuildGradleFile.renameTo(buildGradleFile)
        File("${testProject.path}/build").deleteRecursively()
        File("${testProject.path}/.gradle").deleteRecursively()
    }

    @Test
    fun `When FAT_FRAMEWORK is setup, the fat framework tasks are not present`() {
        val result = runner
            .withArguments(taskName)
            .buildAndFail()

        assertTrue(result.output.contains("Task '$taskName' not found in root project"))
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME),
            arrayOf(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME),
            arrayOf(PublishDebugXCFrameworkTask.NAME),
            arrayOf(PublishReleaseXCFrameworkTask.NAME),
        )
    }
}