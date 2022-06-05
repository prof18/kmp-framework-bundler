package com.prof18.kmp.framework.bundler.testutils

import com.prof18.kmp.framework.bundler.data.FrameworkType
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import java.io.File

abstract class BaseTaskTest(
    private val frameworkType: FrameworkType,
) {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    lateinit var testDestFile: File
    lateinit var runner: GradleRunner

    @Before
    fun setup() {
        testProject = File("src/test/resources/test-project")
        buildGradleFile = File("src/test/resources/test-project/build.gradle.kts")
        testDestFile = File("src/test/resources/test-dest")

        buildGradleFile.writeText(getGradleFile())

        runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        testDestFile.listFiles().forEach { it.deleteRecursively() }
        File("${testProject.path}/build").deleteRecursively()
        File("${testProject.path}/.gradle").deleteRecursively()
    }

    private fun getGradleFile(): String = when(frameworkType) {
        FrameworkType.FAT_FRAMEWORK -> fatFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> legacyXCFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK -> xcFrameworkGradleFile
    }
}
