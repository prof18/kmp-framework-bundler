package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.data.FrameworkType
import com.prof18.kmp.framework.bundler.testutils.BasePublishTest
import com.prof18.kmp.framework.bundler.testutils.FRAMEWORK_VERSION_NUMBER
import com.prof18.kmp.framework.bundler.testutils.POD_SPEC_VERSION_NUMBER
import com.prof18.kmp.framework.bundler.testutils.buildAndRun
import com.prof18.kmp.framework.bundler.testutils.getPlainText
import com.prof18.kmp.framework.bundler.testutils.runBashCommandAndGetOutput
import org.junit.Assert.assertTrue
import org.junit.Test

class XCFrameworkLegacyTasksPublishTests : BasePublishTest(frameworkType = FrameworkType.XC_FRAMEWORK_LEGACY_BUILD) {

    @Test
    fun `When running the publish debug fat framework task, in the destination, the version number is updated in the pod spec, the branch is develop and the commit message is correct`() {
        testProject.buildAndRun(PublishDebugXCFrameworkTask.NAME)

        // version on pod spec
        assertTrue(podSpecFile.getPlainText().contains(POD_SPEC_VERSION_NUMBER))

        // branch name
        val branchOutput = testDestFolder.runBashCommandAndGetOutput("git", "branch", "--list", "develop")
        assertTrue(branchOutput.contains("develop"))

        // commit message
        val commitOutput = testDestFolder.runBashCommandAndGetOutput("git", "log", "-1")
        assertTrue(commitOutput.contains("New debug release: $FRAMEWORK_VERSION_NUMBER -"))
    }

    @Test
    fun `When running the publish release fat framework task, in the destination, the version number is updated in the pod spec, the branch is main, the commit message and the git tag are correct`() {
        testProject.buildAndRun(PublishReleaseXCFrameworkTask.NAME)

        // version on pod spec
        assertTrue(podSpecFile.getPlainText().contains(POD_SPEC_VERSION_NUMBER))

        // branch name
        val branchOutput = testDestFolder.runBashCommandAndGetOutput("git", "branch", "--list", "main")
        assertTrue(branchOutput.contains("main"))

        // commit message
        val commitOutput = testDestFolder.runBashCommandAndGetOutput("git", "log", "-1")
        assertTrue(commitOutput.contains("New release: $FRAMEWORK_VERSION_NUMBER -"))

        // git tag
        val tagOutput = testDestFolder.runBashCommandAndGetOutput("git", "tag")
        assertTrue(tagOutput.contains(FRAMEWORK_VERSION_NUMBER))
    }
}
