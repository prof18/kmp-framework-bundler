package com.prof18.kmp.framework.bundler.task

import com.prof18.kmp.framework.bundler.data.ErrorMessages
import com.prof18.kmp.framework.bundler.testutils.TestUtils
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class TaskSetupTests {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder

    @Before
    fun setup() {
        val testProjectName = "test-project"
        testProject = File("src/test/resources/$testProjectName")
        buildGradleFile = File("src/test/resources/$testProjectName/build.gradle.kts")

        gradleFileStringBuilder = StringBuilder()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        File("${testProject.path}/build").deleteRecursively()
        File("${testProject.path}/.gradle").deleteRecursively()
    }

    @Test
    fun `When the framework name is not specified, an exception is raised`() {
        val pluginConfig = """
           frameworkBundlerConfig {
           }
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.baseGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.FRAMEWORK_NAME_NOT_PRESENT_MESSAGE))
    }

    @Test
    fun `When the output path is not specified, an exception is raised`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
           }
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.baseGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.OUTPUT_PATH_NOT_PRESENT_MESSAGE))
    }

    @Test
    fun `When the version name is not specified, an exception is raised`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
           }
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.baseGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.VERSION_NAME_NOT_PRESENT_MESSAGE))
    }

    @Test
    fun `When an XCFramework is setup and the frameworkType is XC_FRAMEWORK_LEGACY_BUILD, a message is logged`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.xcFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner.build()

        assertTrue(result.output.contains(ErrorMessages.USING_LEGACY_BUILD_SYSTEM))
    }

    @Test
    fun `When an XCFramework is not setup and the frameworkType is XC_FRAMEWORK, an exception is raised`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.baseGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.EMPTY_XC_FRAMEWORK_TASKS))
    }

    @Test
    fun `generate cocoa repo return error when gitUrl is not present`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmp.framework.bundler.data.FrameworkType.XC_FRAMEWORK
                cocoaPodRepoInfo {
                    summary.set("This is a test KMP framework")
                    homepage.set("https://www.site.com")
                    license.set("Apache")
                    authors.set("\"Marco Gomiero\" => \"mg@me.com\"")
                }
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(TestUtils.xcFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.writeText(gradleFileStringBuilder.toString())

        val runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()

        val result = runner
            .withArguments(GenerateCocoaPodRepositoryTask.NAME)
            .buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.GIT_URL_NOT_PRESENT))
    }
}