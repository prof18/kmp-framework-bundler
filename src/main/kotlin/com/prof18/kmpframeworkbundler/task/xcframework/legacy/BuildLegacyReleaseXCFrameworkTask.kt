package com.prof18.kmpframeworkbundler.task.xcframework.legacy

import com.prof18.kmpframeworkbundler.task.xcframework.BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME
import com.prof18.kmpframeworkbundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmpframeworkbundler.data.PluginConfig
import org.gradle.api.Project
import org.gradle.api.tasks.Exec

internal fun Project.registerBuildLegacyReleaseXCFrameworkTask(
    config: PluginConfig
) {
    tasks.register(BUILD_RELEASE_XC_FRAMEWORK_TASK_NAME, Exec::class.java) {
        group = PLUGIN_TASKS_GROUP
        description = "Create a Release XCFramework"

        buildXCFramework(config, config.releaseFrameworkList)
    }
}