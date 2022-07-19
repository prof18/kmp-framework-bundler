package com.prof18.kmpframeworkbundler.task

import com.prof18.kmpframeworkbundler.data.ErrorMessages
import com.prof18.kmpframeworkbundler.testutils.baseFatFrameworkGradleFile
import com.prof18.kmpframeworkbundler.testutils.baseXCFrameworkGradleFile
import com.prof18.kmpframeworkbundler.testutils.buildAndFail
import com.prof18.kmpframeworkbundler.testutils.buildAndRun
import com.prof18.kmpframeworkbundler.testutils.deleteXCFrameworkImport
import com.prof18.kmpframeworkbundler.testutils.resetKotlinVersionToDefault
import com.prof18.kmpframeworkbundler.testutils.setKotlinVersion
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class TaskSetupTests {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder
    private lateinit var tempBuildGradleFile: File

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
    fun `When the framework name is not specified, an exception is raised`() {
        val pluginConfig = """
           frameworkBundlerConfig {
           }
       """.trimIndent()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.FRAMEWORK_NAME_NOT_PRESENT_MESSAGE))
    }

    @Test
    fun `When the output path is not specified, an exception is raised`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
           }
       """.trimIndent()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail()

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

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.VERSION_NAME_NOT_PRESENT_MESSAGE))
    }

    @Test
    fun `When an XCFramework is setup and the frameworkType is XC_FRAMEWORK_LEGACY_BUILD, a message is logged`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseXCFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndRun()

        assertTrue(result.output.contains(ErrorMessages.USING_LEGACY_BUILD_SYSTEM))
    }

    @Test
    fun `When an XCFramework is not setup and the frameworkType is XC_FRAMEWORK, an exception is raised`() {
        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.EMPTY_XC_FRAMEWORK_TASKS))
    }

    @Test
    fun `generate cocoa repo return error when gitUrl is not present`() {

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK
                cocoaPodRepoInfo {
                    summary.set("This is a test KMP framework")
                    homepage.set("https://www.site.com")
                    license.set("Apache")
                    authors.set("\"Marco Gomiero\" => \"mg@me.com\"")
                }
           }     
       """.trimIndent()

        gradleFileStringBuilder.append(baseXCFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail(GenerateCocoaPodRepositoryTask.NAME)

        assertTrue(result.output.contains(ErrorMessages.GIT_URL_NOT_PRESENT))
    }

    @Test
    fun `When kotlin version is less then 1_5_30 and the frameworkType is XC_FRAMEWORK, an exception is raised`() {

        setKotlinVersion("1.5.20")

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK
           }     
       """.trimIndent()

        buildGradleFile.deleteXCFrameworkImport()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail()

        assertTrue(result.output.contains(ErrorMessages.EMPTY_XC_FRAMEWORK_TASKS))

        resetKotlinVersionToDefault()
    }

    @Test
    fun `When kotlin version is less then 1_5_30 and the frameworkType is XC_FRAMEWORK_LEGACY, the project is correctly setup`() {

        setKotlinVersion("1.5.20")

        val pluginConfig = """
           frameworkBundlerConfig {
                frameworkName.set("LibraryName")
                outputPath.set("${testProject.path}/../test-dest")
                versionName.set("1.0.0")
                frameworkType = com.prof18.kmpframeworkbundler.data.FrameworkType.XC_FRAMEWORK_LEGACY_BUILD
           }     
       """.trimIndent()

        buildGradleFile.deleteXCFrameworkImport()

        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
        gradleFileStringBuilder.append("\n")
        gradleFileStringBuilder.append(pluginConfig)
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndRun()

        assertTrue(result.output.contains("BUILD SUCCESSFUL"))

        resetKotlinVersionToDefault()
    }
}
