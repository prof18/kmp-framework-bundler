package com.prof18.kmp.framework.bundler.task.xcframework.legacy

import com.prof18.kmp.framework.bundler.task.xcframework.BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME
import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

internal fun Project.registerBuildLegacyDebugXCFrameworkTask(
    config: PluginConfig
) {
    tasks.register(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME, Exec::class.java) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Debug XCFramework"

        buildXCFramework(config, config.debugFrameworkList)
    }
}