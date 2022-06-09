package com.prof18.kmp.framework.bundler.testutils

import com.prof18.kmp.framework.bundler.data.FrameworkType
import org.gradle.testkit.runner.GradleRunner
import org.junit.After
import org.junit.Before
import java.io.File
import java.nio.file.Paths

abstract class BasePublishTest(
    private val frameworkType: FrameworkType,
) {

    private lateinit var testProject: File
    private lateinit var buildGradleFile: File
    lateinit var testDestFolder: File
    private lateinit var remoteDestFolder: File
    lateinit var podSpecFile: File
    lateinit var runner: GradleRunner

    @Before
    fun setup() {
        testProject = File("src/test/resources/test-project")
        buildGradleFile = File("src/test/resources/test-project/build.gradle.kts")

        val currentPath = Paths.get("").toAbsolutePath().toString()
        testDestFolder = File("$currentPath/../test-dest")
        testDestFolder.mkdirs()

        remoteDestFolder = File("$currentPath/../remote-dest")
        remoteDestFolder.mkdirs()

        buildGradleFile.writeText(getGradleFile())

        testDestFolder.runBashCommand("git", "init")
        testDestFolder.runBashCommand("git", "branch", "-m", "main")

        podSpecFile = File("${testDestFolder.path}/LibraryName.podspec")
        podSpecFile.writeText(getPodSpec())

        testDestFolder.runBashCommand("git", "add", ".")
        testDestFolder.runBashCommand("git", "commit", "-m", "\"First commit\"")

        remoteDestFolder.runBashCommand("git", "init", "--bare")

        testDestFolder.runBashCommand("git", "remote", "add", "origin", remoteDestFolder.path)
        testDestFolder.runBashCommand("git", "push", "origin", "--all")

        testDestFolder.runBashCommand("git", "checkout", "-b", "develop")

        runner = GradleRunner.create()
            .withProjectDir(testProject)
            .withPluginClasspath()
    }

    @After
    fun cleanUp() {
        buildGradleFile.deleteRecursively()
        testDestFolder.deleteRecursively()
        remoteDestFolder.deleteRecursively()
        File("${testProject.path}/build").deleteRecursively()
        File("${testProject.path}/.gradle").deleteRecursively()
    }

    private fun getGradleFile(): String = when (frameworkType) {
        FrameworkType.FAT_FRAMEWORK -> fatFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> legacyXCFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK -> xcFrameworkGradleFile
    }

    private fun getPodSpec(): String = when (frameworkType) {
        FrameworkType.XC_FRAMEWORK, FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> xcFrameworkPodSpec
        FrameworkType.FAT_FRAMEWORK -> fatFrameworkPodSpec
    }
}
