package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.Project

internal const val BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME = "buildReleaseXCFramework"

internal fun Project.registerBuildReleaseXCFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Release XCFramework"

        val debugBuildTask = config.getXCFrameworkReleaseBuildTask()
        dependsOn(debugBuildTask.name)
    }
}
