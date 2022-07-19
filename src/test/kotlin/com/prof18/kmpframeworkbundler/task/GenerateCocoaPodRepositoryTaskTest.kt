package com.prof18.kmpframeworkbundler.task

import com.prof18.kmpframeworkbundler.data.ErrorMessages
import com.prof18.kmpframeworkbundler.testutils.baseFatFrameworkGradleFile
import com.prof18.kmpframeworkbundler.testutils.buildAndFail
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class GenerateCocoaPodTaskWithoutFieldsTest {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    private lateinit var gradleFileStringBuilder: StringBuilder
    private lateinit var tempBuildGradleFile: File

    @Before
    fun setup() {
        testProject = File("src/test/resources/test-project")
        buildGradleFile = File("src/test/resources/test-project/build.gradle.kts")
        tempBuildGradleFile = File("src/test/resources/test-project/build.gradle.kts.new")
        buildGradleFile.copyTo(tempBuildGradleFile)

        gradleFileStringBuilder = StringBuilder()
        gradleFileStringBuilder.append(baseFatFrameworkGradleFile)
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        tempBuildGradleFile.renameTo(buildGradleFile)
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
        buildGradleFile.appendText(gradleFileStringBuilder.toString())

        val result = testProject.buildAndFail(GenerateCocoaPodRepositoryTask.NAME)

        assertTrue(result.output.contains(ErrorMessages.VERSION_NAME_NOT_PRESENT_MESSAGE))
    }
}
