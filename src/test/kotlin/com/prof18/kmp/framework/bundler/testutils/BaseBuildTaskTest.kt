package com.prof18.kmp.framework.bundler.testutils

import com.prof18.kmp.framework.bundler.data.FrameworkType
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import java.io.File
import java.nio.file.Paths


abstract class BaseBuildTaskTest(
    private val frameworkType: FrameworkType,
) {

    private lateinit var buildGradleFile: File
    private lateinit var tempBuildGradleFile: File

    lateinit var testProject: File
    lateinit var testDestFile: File
    lateinit var runner: GradleRunner

    @Before
    open fun setup() {
        testProject = File("src/test/resources/test-project")
        buildGradleFile = File("src/test/resources/test-project/build.gradle.kts")
        tempBuildGradleFile = File("src/test/resources/test-project/build.gradle.kts.new")
        buildGradleFile.copyTo(tempBuildGradleFile)

        val currentPath = Paths.get("").toAbsolutePath().toString()
        testDestFile = File("$currentPath/../test-dest")
        testDestFile.mkdirs()

        // todo
//        if (true) {
//            val contentLines = buildGradleFile.readLines()
//
//            buildGradleFile.outputStream().use {
//                it.write("".toByteArray())
//            }
//
//            buildGradleFile.appendText("\n")
//            contentLines
//                .drop(1)
//                .filter { it.isNotEmpty() }
//                .forEach {
//                    buildGradleFile.appendText(it)
//                    buildGradleFile.appendText("\n")
//                }
//        }

        buildGradleFile.appendText(getGradleFile())

        runner = GradleRunner.create()
            .withProjectDir(testProject)
//            .withPluginClasspath()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        tempBuildGradleFile.renameTo(buildGradleFile)
        testDestFile.deleteRecursively()
        File("${testProject.path}/build").deleteRecursively()
        File("${testProject.path}/.gradle").deleteRecursively()
    }

    private fun getGradleFile(): String = when (frameworkType) {
        FrameworkType.FAT_FRAMEWORK -> fatFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> legacyXCFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK -> xcFrameworkGradleFile
    }
}
