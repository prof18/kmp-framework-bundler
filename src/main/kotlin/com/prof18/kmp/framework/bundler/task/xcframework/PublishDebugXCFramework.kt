package com.prof18.kmp.framework.bundler.task.xcframework

import com.prof18.kmp.framework.bundler.utils.PLUGIN_TASKS_GROUP
import com.prof18.kmp.framework.bundler.data.PluginConfig
import com.prof18.kmp.framework.bundler.task.common.PrepareCocoaRepoForDebugTasks
import com.prof18.kmp.framework.bundler.task.common.publishDebugFramework
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

internal abstract class PublishDebugXCFrameworkTask @Inject constructor(
    private val pluginConfig: PluginConfig,
) : DefaultTask() {

    init {
        group = PLUGIN_TASKS_GROUP
        description = "Publish a Debug XCFramework to the CocoaPod Repository"
    }

    @TaskAction
    fun action() {
        project.publishDebugFramework(pluginConfig)
    }

    internal companion object {
        const val NAME: String = "publishDebugXCFramework"
    }
}

internal fun Project.registerPublishDebugXCFrameworkTask(
    pluginConfig: PluginConfig,
) {
    tasks.register(
        PublishDebugXCFrameworkTask.NAME,
        PublishDebugXCFrameworkTask::class.java,
        pluginConfig,
    ).apply {
        configure {
            mustRunAfter(PrepareCocoaRepoForDebugTasks.NAME)
            dependsOn(PrepareCocoaRepoForDebugTasks.NAME)
            dependsOn(BUILD_DEBUG_XC_FRAMEWORK_TASK_NAME)
        }
    }
}