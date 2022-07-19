package com.prof18.kmpframeworkbundler.testutils

import com.prof18.kmpframeworkbundler.data.FrameworkType
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
    var hasToDeleteImport: Boolean = false

    @Before
    open fun setup() {
        testProject = File("src/test/resources/test-project")
        buildGradleFile = File("src/test/resources/test-project/build.gradle.kts")
        tempBuildGradleFile = File("src/test/resources/test-project/build.gradle.kts.new")
        buildGradleFile.copyTo(tempBuildGradleFile)

        val currentPath = Paths.get("").toAbsolutePath().toString()
        testDestFile = File("$currentPath/../test-dest")
        testDestFile.mkdirs()

        if (hasToDeleteImport) {
            buildGradleFile.deleteXCFrameworkImport()
        }

        buildGradleFile.appendText(getGradleFile())
    }

    @After
    open fun cleanUp() {
        buildGradleFile.deleteRecursively()
        tempBuildGradleFile.renameTo(buildGradleFile)
        testDestFile.deleteRecursively()
        File("${testProject.path}/build").deleteRecursively()
        hasToDeleteImport = false
    }

    private fun getGradleFile(): String = when (frameworkType) {
        FrameworkType.FAT_FRAMEWORK -> fatFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK_LEGACY_BUILD -> legacyXCFrameworkGradleFile
        FrameworkType.XC_FRAMEWORK -> xcFrameworkGradleFile
    }
}
