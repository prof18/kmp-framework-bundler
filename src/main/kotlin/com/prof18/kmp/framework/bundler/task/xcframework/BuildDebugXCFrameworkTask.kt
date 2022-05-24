package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.Project

internal const val BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME = "buildDebugXCFramework"

internal fun Project.registerBuildDebugXCFrameworkTask(
    config: PluginConfig,
) {
    tasks.register(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Debug XCFramework"

        val debugBuildTask = config.getXCFrameworkDebugBuildTask()
        dependsOn(debugBuildTask.name)
    }
}
