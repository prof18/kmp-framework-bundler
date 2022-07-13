package com.prof18.kmp.framework.bundler

import com.prof18.kmp.framework.bundler.task.xcframework.BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.testutils.gradlew
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class MultiversionTest {

    @JvmField
    @Rule
    var folder = TemporaryFolder()

    private lateinit var projectFolder: File

    @Before
    fun setup() {
//        System.setProperty("kotlinVersion", "1.5.0");

        println("version: ${System.getProperty("kotlinVersion")}")

        projectFolder = File("src/test/resources/test-project")
    }

    @Test
    fun test() {

        val output = projectFolder.gradlew(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME).output

        println(output)

    }

    /*

        TODO: muovere il set della version di kotlin dentro il progetto
            - testare che in qualche modo ci siano i task
            - testare cosa succede con vecchie versioni di kotlin
            - testare solo con build?

     */


}

