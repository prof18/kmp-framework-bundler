package com.prof18.kmp.framework.bundler.task

import com.prof18.kmp.framework.bundler.data.ErrorMessages
import com.prof18.kmp.framework.bundler.testutils.baseFatFrameworkGradleFile
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class GenerateCocoaPodTaskWithoutFieldsTest {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder

    @Before
    fun setup() {
        val testProjectName = "test-project"
        testProject = File("src/test/resources/$testProjectName")
        buildGradleFile = File("src/test/resources/$testProjectName/build.gradle.kts")

        gradleFileStringBuilder = StringBuilder()
        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
    }

    @Test
    fun `generate cocoa repo return error when gitUrl is not present`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
           }
       """.trimIndent()

        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner
            .withArguments(GenerateCocoaPodRepositoryTask.NAME, "--stacktrace")
            .buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.VERSION_NAME_NOT_PRESENT_MESSAGE))
    }
}